package com.khaipv.attendance.model

import com.google.gson.annotations.SerializedName

data class DetailAttendanceStudent(
    @SerializedName("startTime") var startTime: String = "",
    @SerializedName("endTime") var endTime: String = "",
    @SerializedName("timeAttendance") var timeAttendance: String? = null,
    @SerializedName("name") var classroomName: String = "",
    @SerializedName("scheduleId") var scheduleId: Int = 0,
)
