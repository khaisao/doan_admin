package com.khaipv.attendance.model

import com.google.gson.annotations.SerializedName

data class DetailAttendanceStudentTeacherScreen(
    @SerializedName("studentId") var studentId: Int = 0,
    @SerializedName("studentName") var studentName: String = "",
    @SerializedName("timeAttendance") var timeAttendance: String? = null,
    @SerializedName("startTime") var startTime: String? = null,
)
