package com.example.baseproject.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("accountId")
    val accountId: Int = 0,
    @SerializedName("studentId")
    val studentId: Int = 0,
    @SerializedName("teacherId")
    val teacherId: Int = 0,
    @SerializedName("adminId")
    val adminId: Int = 0,
    @SerializedName("avatar")
    val avatar: String = "",
    @SerializedName("studentName")
    val studentName: String = "",
    @SerializedName("teacherName")
    val teacherName: String = "",
    @SerializedName("adminName")
    val adminName: String = "",
    @SerializedName("userName")
    val userName: String = "",
    @SerializedName("password")
    val password: String = "",
    @SerializedName("role")
    val role: Int = 0,
    @SerializedName("token")
    val token: String = ""
)