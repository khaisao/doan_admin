package com.example.baseproject.model

import com.google.gson.annotations.SerializedName

data class OverviewCourseTeacherAssign(
    @SerializedName("cycleId")
    val cycleId: Int = 0,
    @SerializedName("cycleStartDate")
    val cycleStartDate: String = "",
    @SerializedName("cycleEndDate")
    val cycleEndDate: String = "",
    @SerializedName("cyclesDes")
    val cyclesDes: String = "",
    @SerializedName("listCourse")
    val listCourse: List<DetailCourseTeacherAssign> = emptyList(), )
