package com.khaipv.attendance.model

import com.google.gson.annotations.SerializedName

data class DataImageProfileResponse(
    @SerializedName("studentId") var studentId: Int,
    @SerializedName("listDataImageProfile") var listDataImageProfile: List<String> = emptyList()
)
