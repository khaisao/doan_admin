package com.example.baseproject.network

import com.example.baseproject.model.ApiObjectResponse
import com.example.baseproject.model.LoginResponse
import com.google.gson.JsonObject

import okhttp3.RequestBody
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST("api/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): ApiObjectResponse<LoginResponse>
}
