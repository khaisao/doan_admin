package com.khaipv.attendance.model

import com.google.gson.annotations.SerializedName

data class AddImageProfileBody(
    @SerializedName("studentId") val studentId: Int,
    @SerializedName("listImageData") val listImageData: List<String>,
    @SerializedName("modelMode") val modelMode: Int
)
