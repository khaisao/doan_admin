package com.example.baseproject.model

import com.google.gson.annotations.SerializedName

data class ImageProfileResponse(
    @SerializedName("studentId") var studentId: Int,
    @SerializedName("listImageUrl") var listImageUrl: List<String> = emptyList()
)
