package com.khaipv.attendance.model

import com.google.gson.annotations.SerializedName

data class ProfileUrl(
    @SerializedName("url_1")
    val url1: String = "",
    @SerializedName("url_2")
    val url2: String = "",
    @SerializedName("url_3")
    val url3: String = ""
)