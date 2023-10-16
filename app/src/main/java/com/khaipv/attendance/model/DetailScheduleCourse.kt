package com.khaipv.attendance.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailScheduleCourse(
    @SerializedName("coursePerCycleId")
    val coursePerCycleId: Int = 0,
    @SerializedName("scheduleId")
    val scheduleId: Int = 0,
    @SerializedName("startTime")
    val startTime: String = "",
    @SerializedName("endTime")
    val endTime: String = "",
    @SerializedName("name")
    val classroomName: String = "",
    @SerializedName("courseName")
    val courseName: String = "",
) : Parcelable
