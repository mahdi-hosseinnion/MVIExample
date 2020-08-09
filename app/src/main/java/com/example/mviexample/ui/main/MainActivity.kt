package com.example.mviexample.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.lifecycle.ViewModelProvider
import com.example.mviexample.R
import com.example.mviexample.ui.DataStateListener
import com.example.mviexample.util.DataState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    DataStateListener {
    override fun onDataStateChange(dataState: DataState<*>?) {
        handleDataStateChanged(dataState);
    }

    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        showMainFragment()
    }

    private fun showMainFragment() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                MainFragment(), "MainFragment"
            )
            .commit();
    }

    private fun handleDataStateChanged(dataState: DataState<*>?) {
        dataState?.let {
            //handle loading
            showProgressBar(it.loading)
            //handle error
            it.message?.let {eventMsg->
                eventMsg.getContentIfNotHandled()?.let {message->
                    showToast(message)
                }
            }
        }

    }

    private fun showToast(message: String?) {
        message?.let {
            Toast.makeText(this, it, LENGTH_LONG).show()
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        if (isLoading) {
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.GONE
        }
    }

}