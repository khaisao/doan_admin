package com.example.baseproject.model

import com.google.gson.annotations.SerializedName

data class AttendanceBody(
    @SerializedName("studentId") val studentId: Int,
    @SerializedName("registrationId") val registrationId: Int,

)
