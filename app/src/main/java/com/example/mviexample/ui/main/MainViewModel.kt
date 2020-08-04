package com.example.mviexample.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mviexample.ui.main.state.MainViewState

class MainViewModel : ViewModel() {

    private val _viewState: MutableLiveData<MainViewState> = MutableLiveData()

    val viewState: MutableLiveData<MainViewState>
        get() = _viewState

}