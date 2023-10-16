package com.khaipv.attendance.network

import com.google.gson.annotations.SerializedName

class LoginResponse : BaseResponse() {

    @SerializedName("token")
    val token: String = ""

    @SerializedName("name")
    val name: String = ""

}
