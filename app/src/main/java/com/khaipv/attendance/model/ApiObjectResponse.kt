package com.khaipv.attendance.model

import com.google.gson.annotations.SerializedName

data class ApiObjectResponse<T>(
    @SerializedName("errors") var errors: List<String>,
    @SerializedName("data") var dataResponse: T,
)

