package com.example.baseproject.model

import com.google.gson.annotations.SerializedName

data class AllImageProfileStudentForCourse(
    @SerializedName("studentId")
    val studentId: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("profileUrl")
    val profileUrl: ProfileUrl? = null
)