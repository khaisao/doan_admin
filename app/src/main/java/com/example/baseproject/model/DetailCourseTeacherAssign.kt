package com.example.baseproject.model

import com.google.gson.annotations.SerializedName

data class DetailCourseTeacherAssign(
    @SerializedName("teacherId")
    val teacherId: Int = 0,
    @SerializedName("teacherName")
    val teacherName: String = "",
    @SerializedName("dateOfBirth")
    val dateOfBirth: String = "",
    @SerializedName("avatar")
    val avatar: String = "",
    @SerializedName("accountId")
    val accountId: Int = 0,
    @SerializedName("teacherPerCourseId")
    val teacherPerCourseId: Int = 0,
    @SerializedName("coursePerCycleId")
    val coursePerCycleId: Int = 0,
    @SerializedName("courseId")
    val courseId: Int = 0,
    @SerializedName("cycleId")
    val cycleId: Int = 0,
    @SerializedName("courseName")
    val courseName: String = "",
)
