package com.example.baseproject.model

import com.google.gson.annotations.SerializedName

data class TeacherInfoResponse(
    @SerializedName("teacherId") var teacherId: Int = 0,
    @SerializedName("teacherName") var teacherName: String = "",
    @SerializedName("dateOfBirth") var dateOfBirth: String = "",
    @SerializedName("avatar") var avatar: String = "",
    @SerializedName("accountId") var accountId: Int = 0,
)
