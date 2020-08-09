package com.example.mviexample.api

import com.example.mviexample.model.BlogPost
import com.example.mviexample.model.User
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("placeholder/user/{userId}")
    fun getUser(@Path("userId") userId: String): User

    @GET("placeholder/blogs")
    fun getBlogPosts(): List<BlogPost>

}