package com.example.mviexample.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.mviexample.api.RetrofitBuilder
import com.example.mviexample.model.BlogPost
import com.example.mviexample.model.User
import com.example.mviexample.ui.main.state.MainViewState
import com.example.mviexample.util.*

object Repositry {
    fun getBlogPosts(): LiveData<DataState<MainViewState>> {
        return object : NetworkBoundResource<List<BlogPost>, MainViewState>() {
            override fun handleApiSuccessResponse(response: ApiSuccessResponse<List<BlogPost>>) {
                result.value = DataState.data(
                    message = null,
                    data = MainViewState(
                        blogPost = response.body
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<List<BlogPost>>> {
                return RetrofitBuilder.apiService.getBlogPosts()
            }
        }.asLiveData()

    }

    fun getUser(userId: String): LiveData<DataState<MainViewState>> {
        return return object : NetworkBoundResource<User, MainViewState>() {
            override fun handleApiSuccessResponse(response: ApiSuccessResponse<User>) {
                result.value = DataState.data(
                    message = null,
                    data = MainViewState(
                        user = response.body
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<User>> {
                return RetrofitBuilder.apiService.getUser(userId = userId)
            }
        }.asLiveData()

    }
}