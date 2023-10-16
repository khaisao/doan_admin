package com.khaipv.attendance.model

import com.google.gson.annotations.SerializedName

data class CourseHaveShedule(
    @SerializedName("coursePerCycleId") var coursePerCycleId: Int,
    @SerializedName("courseId") var courseId: Int,
    @SerializedName("cyclesDes") var cyclesDes: String,
    @SerializedName("courseName") var courseName: String,
    @SerializedName("teacherId") var teacherId: Int,
    @SerializedName("teacherName") var teacherName: String,
    @SerializedName("dateOfBirth") var dateOfBirth: String,
    @SerializedName("avatar") var avatar: String,
    @SerializedName("accountId") var accountId: Int
)
