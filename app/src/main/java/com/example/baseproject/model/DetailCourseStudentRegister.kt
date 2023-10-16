package com.example.baseproject.model

import com.google.gson.annotations.SerializedName

data class DetailCourseStudentRegister(
    @SerializedName("studentId")
    val studentId: Int,
    @SerializedName("studentName")
    val studentName: String,
    @SerializedName("dateOfBirth")
    val dateOfBirth: String,
    @SerializedName("registrationId")
    val registrationId: Int,
    @SerializedName("coursePerCycleId")
    val coursePerCycleId: Int,
    @SerializedName("courseId")
    val courseId: Int,
    @SerializedName("cycleId")
    val cycleId: Int,
    @SerializedName("courseName")
    val courseName: String,
)