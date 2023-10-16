package com.khaipv.attendance.model

import com.google.gson.annotations.SerializedName

data class AccountStudentResponse(
    @SerializedName("studentId")
    val studentId: Int,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("studentName")
    val studentName: String,
    @SerializedName("dateOfBirth")
    val dateOfBirth: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("accountId")
    val accountId: Int,
    @SerializedName("role")
    val role: Int,
)