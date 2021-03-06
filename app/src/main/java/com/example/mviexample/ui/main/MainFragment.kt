package com.example.mviexample.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mviexample.R
import com.example.mviexample.model.BlogPost
import com.example.mviexample.model.User
import com.example.mviexample.ui.DataStateListener
import com.example.mviexample.ui.main.state.MainStateEvent
import com.example.mviexample.ui.main.state.MainViewState
import com.example.mviexample.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_main.*
import java.lang.ClassCastException

class MainFragment : Fragment() ,
    BlogPostsReyclerAdapter.Interaction
{
    //var
    lateinit var viewModel: MainViewModel
    lateinit var dataStateHandler: DataStateListener
    lateinit var recyclerAdapter:BlogPostsReyclerAdapter


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
        initRecycler()
        subscribeObservers()
    }
    private fun initRecycler(){
        recycler_view.apply {
            layoutManager=LinearLayoutManager(activity)
            recyclerAdapter=BlogPostsReyclerAdapter(this@MainFragment)
            val topSpacingItemDecoration=TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            adapter=recyclerAdapter
        }
    }
    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            println("DEBUG: DataState: $dataState")
            //handle loading and error
            dataStateHandler.onDataStateChange(dataState)
            //handle data<T>
            dataState.data?.let { event ->
                event.getContentIfNotHandled()?.let { mainViewState ->
                    mainViewState.blogPost?.let {
                        //set BlogPost Data
                        viewModel.setBlogListData(it)
                    }
                    mainViewState.user?.let {
                        //set User Data
                        viewModel.setUserData(it)
                    }
                }
            }
        })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.blogPost?.let {
                println("DEBUG: Setting blog posts to RecyclerView: $it")
                recyclerAdapter.submitList(it)
            }
            viewState.user?.let {
                println("DEBUG: Setting User Data: $it")
                setUserProperties(it)
            }
        })
    }

    private fun setUserProperties(user:User){
        email.text = user.email
        username.text = user.username
        view?.let {
            Glide.with(it.context)
                .load(user.image)
                .into(image)
        }
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dataStateHandler = context as DataStateListener
        } catch (e: ClassCastException) {
            println("DEBUG: $context must implement DataStateListener")
        }
    }

    override fun onItemSelected(position: Int, item: BlogPost) {
        println("DEBUG: the item #$position clicked")
        println("DEBUG: $item")
    }
}