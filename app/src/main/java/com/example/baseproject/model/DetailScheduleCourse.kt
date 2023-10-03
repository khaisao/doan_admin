package com.example.baseproject.model

import com.google.gson.annotations.SerializedName

data class DetailScheduleCourse(
    @SerializedName("coursePerCycleId")
    val coursePerCycleId: Int = 0,
    @SerializedName("scheduleId")
    val scheduleId: Int = 0,
    @SerializedName("startTime")
    val startTime: String = "",
    @SerializedName("endTime")
    val endTime: String = "",
    @SerializedName("name")
    val classroomName: String = "",
    @SerializedName("courseName")
    val courseName: String = "",
)
