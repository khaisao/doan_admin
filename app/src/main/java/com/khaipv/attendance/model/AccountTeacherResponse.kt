package com.khaipv.attendance.model

import com.google.gson.annotations.SerializedName

data class AccountTeacherResponse(
    @SerializedName("teacherId")
    val teacherId: Int,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("teacherName")
    val teacherName: String,
    @SerializedName("dateOfBirth")
    val dateOfBirth: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("accountId")
    val accountId: Int,
    @SerializedName("role")
    val role: Int,
)