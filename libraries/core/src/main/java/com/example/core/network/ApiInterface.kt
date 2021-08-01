package com.example.core.network

import com.example.core.model.network.ApiUser
import retrofit2.http.GET

interface ApiInterface {
    @GET("users")
     fun getUsers(): List<ApiUser>

    @GET("more-users")
    suspend fun getMoreUsers(): List<ApiUser>

    @GET("error")
    suspend fun getUsersWithError(): List<ApiUser>
}
