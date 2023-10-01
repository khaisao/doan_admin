package com.example.baseproject.model

import com.google.gson.annotations.SerializedName

data class DetailScheduleStudent(
    @SerializedName("startTime") var startTime: String = "",
    @SerializedName("endTime") var endTime: String = "",
    @SerializedName("timeAttendance") var timeAttendance: String? = null,
    @SerializedName("name") var classroomName: String = "",
)
