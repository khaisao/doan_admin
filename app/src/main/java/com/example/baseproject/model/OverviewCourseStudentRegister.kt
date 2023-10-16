package com.example.baseproject.model

import com.google.gson.annotations.SerializedName

data class OverviewCourseStudentRegister(
    @SerializedName("cycleId")
    val cycleId: Int,
    @SerializedName("cycleStartDate")
    val cycleStartDate: String,
    @SerializedName("cycleEndDate")
    val cycleEndDate: String,
    @SerializedName("cyclesDes")
    val cyclesDes: String,
    @SerializedName("listCourse")
    val listCourse: List<DetailCourseStudentRegister> = emptyList(),
)