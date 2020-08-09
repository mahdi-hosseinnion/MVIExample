package com.example.mviexample.repository

import android.provider.Settings
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.mviexample.ui.main.state.MainViewState
import com.example.mviexample.util.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class NetworkBoundResource<ResponseObject, ViewStateType> {
    protected val result = MediatorLiveData<DataState<ViewStateType>>()

    init {
        result.value = DataState.loading(true)
        GlobalScope.launch(IO) {
            delay(500)
            withContext(Main) {
                val apiResponse = createCall()
                result.addSource(apiResponse) { response ->
                    result.removeSource(apiResponse)
                    handleNetworkCall(response)

                }

            }
        }

    }

    private fun handleNetworkCall(response: GenericApiResponse<ResponseObject>?) {
        when (response) {
            is ApiSuccessResponse -> {
                handleApiSuccessResponse(response)
            }
            is ApiErrorResponse -> {
                println("DEBUG: Repository: getUser error: ${response.errorMessage}")
               onErrorReturn(response.errorMessage)
            }
            is ApiEmptyResponse -> {
                println("DEBUG: Repository: getUser error: HTTP 204. Returned NOTHING")
                onErrorReturn("HTTP 204. Returned NOTHING")


            }
        }
    }

    private fun onErrorReturn(errorMessage: String) {
        result.value=DataState.error(errorMessage)
    }

    abstract fun handleApiSuccessResponse(response:ApiSuccessResponse<ResponseObject> )

    abstract fun createCall(): LiveData<GenericApiResponse<ResponseObject>>

    fun asLiveData() = result as LiveData<DataState<ViewStateType>>
}