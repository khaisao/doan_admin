package com.example.baseproject.model

import com.google.gson.annotations.SerializedName

data class OverviewScheduleStudent(
    @SerializedName("studentId") var studentId: String = "",
    @SerializedName("studentName") var studentName: String = "",
    @SerializedName("timeAttendance") var timeAttendance: String? = null,
    @SerializedName("schedules") var schedules: List<DetailScheduleStudent> = emptyList(),
)
