package com.example.mviexample.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.mviexample.api.RetrofitBuilder
import com.example.mviexample.ui.main.state.MainViewState
import com.example.mviexample.util.ApiEmptyResponse
import com.example.mviexample.util.ApiErrorResponse
import com.example.mviexample.util.ApiSuccessResponse
import com.example.mviexample.util.DataState

object Repositry {
    fun getBlogPosts(): LiveData<DataState<MainViewState>> {
        return Transformations
            .switchMap(RetrofitBuilder.apiService.getBlogPosts()) { apiResponse ->
                object : LiveData<DataState<MainViewState>>() {
                    override fun onActive() {
                        super.onActive()
                        when (apiResponse) {
                            is ApiSuccessResponse -> {
                                value = DataState.data(
                                    message = null,
                                    data = MainViewState(
                                        blogPost = apiResponse.body
                                    )
                                )
                            }
                            is ApiErrorResponse -> {
                                println("DEBUG: Repository: getBlogPosts error: ${apiResponse.errorMessage}")
                                value = DataState.error(
                                    apiResponse.errorMessage
                                )
                            }
                            is ApiEmptyResponse -> {
                                println("DEBUG: Repository: getBlogPosts error: HTTP 204. Returned NOTHING")
                                value = DataState.error(
                                    "HTTP 204. Returned NOTHING"
                                )                            }
                        }
                    }
                }
            }
    }

    fun getUser(userId: String): LiveData<DataState<MainViewState>> {
        return Transformations
            .switchMap(RetrofitBuilder.apiService.getUser(userId)) { apiResponse ->
                object : LiveData<DataState<MainViewState>>() {
                    override fun onActive() {
                        super.onActive()
                        when (apiResponse) {
                            is ApiSuccessResponse -> {
                                value = DataState.data(
                                    message = null,
                                    data = MainViewState(
                                        user = apiResponse.body
                                    )
                                )
                            }
                            is ApiErrorResponse -> {
                                println("DEBUG: Repository: getUser error: ${apiResponse.errorMessage}")
                                value = DataState.error(
                                    apiResponse.errorMessage
                                )
                            }
                            is ApiEmptyResponse -> {
                                println("DEBUG: Repository: getUser error: HTTP 204. Returned NOTHING")
                                value = DataState.error(
                                    "HTTP 204. Returned NOTHING"
                                )
                            }
                        }
                    }
                }
            }
    }
}