package com.khaipv.attendance.model

import com.google.gson.annotations.SerializedName

data class RegisterAccountRequest(
    @SerializedName("userName") val userName: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String,
    @SerializedName("role") val role: Int,

)
