package com.khaipv.attendance.model

import com.google.gson.annotations.SerializedName

data class OverViewCourseHaveShedule(
    @SerializedName("cycleId") var cycleId: Int,
    @SerializedName("cycleStartDate") var cycleStartDate: String,
    @SerializedName("cycleEndDate") var cycleEndDate: String,
    @SerializedName("cyclesDes") var cyclesDes: String,
    @SerializedName("listCourse") var listCourse: List<CourseHaveShedule> = emptyList(),
)
