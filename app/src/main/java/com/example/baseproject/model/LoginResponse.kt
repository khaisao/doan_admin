package com.example.baseproject.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("password")
    val password: String = "",
    @SerializedName("role")
    val role: Int = 0,
    @SerializedName("token")
    val token: String = ""
)