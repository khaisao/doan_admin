package com.khaipv.attendance.model

import com.google.gson.annotations.SerializedName

data class OverviewScheduleStudent(
    @SerializedName("studentId") var studentId: Int = 0,
    @SerializedName("studentName") var studentName: String = "",
    @SerializedName("schedules") var schedules: List<DetailAttendanceStudent> = emptyList(),
)
