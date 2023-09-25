package com.example.core.model.network

import com.google.gson.annotations.SerializedName

data class ApiUser(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("userName")
    val userName: String = "",
    @SerializedName("avatar")
    val avatar: String = ""
)