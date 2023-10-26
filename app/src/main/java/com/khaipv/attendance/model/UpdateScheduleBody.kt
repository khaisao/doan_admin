package com.khaipv.attendance.model

import com.google.gson.annotations.SerializedName

data class UpdateScheduleBody(
    @SerializedName("classroomId") val classroomId: Int,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("scheduleId") val scheduleId: Int,

)
