package com.example.mviexample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.mviexample.model.BlogPost
import com.example.mviexample.model.User
import com.example.mviexample.model.util.AbsentLiveData
import com.example.mviexample.repository.Repositry
import com.example.mviexample.ui.main.state.MainStateEvent
import com.example.mviexample.ui.main.state.MainStateEvent.*
import com.example.mviexample.ui.main.state.MainViewState
import com.example.mviexample.util.DataState

class MainViewModel : ViewModel() {

    private val _stateEvent: MutableLiveData<MainStateEvent> = MutableLiveData()
    private val _viewState: MutableLiveData<MainViewState> = MutableLiveData()

    val viewState: MutableLiveData<MainViewState>
        get() = _viewState

    val dataState: LiveData<DataState<MainViewState>> = Transformations
        .switchMap(_stateEvent) { stateEvent ->
            stateEvent?.let {
                handleStateEvent(it)
            }
        }

    private fun handleStateEvent(stateEvent: MainStateEvent): LiveData<DataState<MainViewState>> {
        when (stateEvent) {

            is GetBlogPostEvent -> {
                return Repositry.getBlogPosts()
            }
            is GetUserEvent -> {
                return Repositry.getUser(stateEvent.userId)
            }
            is None -> {
                return AbsentLiveData.create()

            }
        }
    }

    private fun getCurrentViewStateOrNew(): MainViewState {
        return viewState.value?.let {
            it
        } ?: MainViewState()
    }

    fun setBlogListData(blogPosts: List<BlogPost>) {
        val update = getCurrentViewStateOrNew()
        update.blogPost = blogPosts
        _viewState.value = update
    }

    fun setUserData(user: User) {
        val update = getCurrentViewStateOrNew()
        update.user = user
        _viewState.value = update
    }

    fun setStateEvent(event: MainStateEvent) {
        _stateEvent.value = event
    }


}