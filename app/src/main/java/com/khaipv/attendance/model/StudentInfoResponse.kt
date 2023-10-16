package com.khaipv.attendance.model

import com.google.gson.annotations.SerializedName

data class StudentInfoResponse(
    @SerializedName("studentId") var teacherId: Int = 0,
    @SerializedName("studentName") var teacherName: String = "",
    @SerializedName("dateOfBirth") var dateOfBirth: String = "",
    @SerializedName("class") var classroom: String = "",
    @SerializedName("avatar") var avatar: String = "",
    @SerializedName("accountId") var accountId: Int = 0,
)
