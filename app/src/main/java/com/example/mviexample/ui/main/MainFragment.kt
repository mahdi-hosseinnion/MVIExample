package com.example.mviexample.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mviexample.R
import com.example.mviexample.ui.main.state.MainStateEvent
import com.example.mviexample.ui.main.state.MainViewState

class MainFragment : Fragment() {
    lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = activity?.run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        } ?: throw Exception("INVALID Activity")
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            println("DEBUG: DataState: $dataState")
            //handle data<T>
            dataState.data?.let { MainViewState ->
                MainViewState.blogPost?.let {
                    //set BlogPost Data
                    viewModel.setBlogListData(it)
                }
                MainViewState.user?.let {
                    //set User Data
                    viewModel.setUserData(it)
                }
            }
            //handle loading
            dataState.loading.let {

            }
            //handle error
            dataState.message?.let {

            }
        })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.blogPost?.let {
                println("DEBUG: Setting blog posts to RecyclerView: $it")
            }
            viewState.user?.let {
                println("DEBUG: Setting User Data: $it")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_getBlogs -> triggerGetBlogsEvent()
            R.id.action_getUser -> triggerGetUserEvent()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun triggerGetBlogsEvent() {
        viewModel.setStateEvent(MainStateEvent.GetBlogPostEvent())
    }

    private fun triggerGetUserEvent() {
        viewModel.setStateEvent(MainStateEvent.GetUserEvent("1"))
    }


}