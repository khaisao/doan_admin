package com.khaipv.attendance.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginRequest(
    @SerializedName("userName")
    var userName: String = "",
    @SerializedName("password")
    var password: String = "",
) : Parcelable
