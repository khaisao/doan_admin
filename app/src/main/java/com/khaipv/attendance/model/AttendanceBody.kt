package com.khaipv.attendance.model

import com.google.gson.annotations.SerializedName

data class AttendanceBody(
    @SerializedName("studentId") val studentId: List<Int>,
    @SerializedName("registrationId") val registrationId: Int,
    @SerializedName("scheduleId") val scheduleId: Int,

)
