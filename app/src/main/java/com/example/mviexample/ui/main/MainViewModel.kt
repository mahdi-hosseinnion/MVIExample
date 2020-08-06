package com.example.mviexample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.mviexample.model.BlogPost
import com.example.mviexample.model.User
import com.example.mviexample.model.util.AbsentLiveData
import com.example.mviexample.ui.main.state.MainStateEvent
import com.example.mviexample.ui.main.state.MainStateEvent.*
import com.example.mviexample.ui.main.state.MainViewState

class MainViewModel : ViewModel() {

    private val _stateEvent: MutableLiveData<MainStateEvent> = MutableLiveData()
    private val _viewState: MutableLiveData<MainViewState> = MutableLiveData()

    val viewState: MutableLiveData<MainViewState>
        get() = _viewState

    val dataState: LiveData<MainViewState> = Transformations
        .switchMap(_stateEvent) { stateEvent ->
            stateEvent?.let {
                handleStateEvent(it)
            }
        }

    private fun handleStateEvent(stateEvent: MainStateEvent): LiveData<MainViewState> {
        when (stateEvent) {

            is GetBlogPostEvent -> {
                return object:LiveData<MainViewState>(){
                    override fun onActive() {
                        super.onActive()
                        val blogList:ArrayList<BlogPost> =ArrayList()
                        blogList.add(
                            BlogPost(
                                pk = 0,
                                title = "Vancouver PNE 2019",
                                body = "Here is Jess and I at the Vancouver PNE. We ate a lot of food.",
                                image = "https://cdn.open-api.xyz/open-api-static/static-blog-images/image8.jpg"
                            )
                        )
                        blogList.add(
                            BlogPost(
                                pk = 1,
                                title = "Ready for a Walk",
                                body = "Here I am at the park with my dogs Kiba and Maizy. Maizy is the smaller one and Kiba is the larger one.",
                                image = "https://cdn.open-api.xyz/open-api-static/static-blog-images/image2.jpg"
                            )
                        )
                        value= MainViewState(
                            blogList
                        )

                    }
                }
            }
            is GetUserEvent -> {
                return object : LiveData<MainViewState>() {
                    override fun onActive() {
                        super.onActive()
                        val user=User(
                            email = "mitch@tabian.ca",
                            username = "mitch",
                            image = "https://cdn.open-api.xyz/open-api-static/static-random-images/logo_1080_1080.png"
                        )
                        value=MainViewState(
                            user=user
                        )
                    }
                }
            }
            is None -> {
                return AbsentLiveData.create()

            }
        }
    }
    private fun getCurrentViewStateOrNew():MainViewState{
        return viewState.value?.let {
            it
        }?: MainViewState()
    }
    fun setBlogListData(blogPosts:List<BlogPost>){
        val update=getCurrentViewStateOrNew()
        update.blogPost=blogPosts
        _viewState.value=update
    }
    fun setUserData(user: User){
        val update=getCurrentViewStateOrNew()
        update.user=user
        _viewState.value=update
    }
    fun setStateEvent(event:MainStateEvent){
        _stateEvent.value=event
    }


}