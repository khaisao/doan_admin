package com.khaipv.attendance.model

import com.google.gson.annotations.SerializedName

data class Classroom(
    @SerializedName("classroomId") var classroomId: Int = 0,
    @SerializedName("name") var name: String = "",
)
