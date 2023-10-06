package com.example.baseproject.model

import com.google.gson.annotations.SerializedName

data class AddImageProfileBody(
    @SerializedName("studentId") val studentId: Int,
    @SerializedName("listImageData") val listImageData: List<String>,

)
