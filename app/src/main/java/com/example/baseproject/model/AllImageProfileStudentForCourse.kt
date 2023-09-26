package com.example.baseproject.model

import com.google.gson.annotations.SerializedName

data class AllImageProfileStudentForCourse(
    @SerializedName("studentId")
    val studentId: Int = 0,
    @SerializedName("studentName")
    val studentName: String = "",
    @SerializedName("registrationId")
    val registrationId: Int = 0,
    @SerializedName("listImageProfile")
    val listImageProfile: List<String> = emptyList(),
    var isReco:Boolean = false
)