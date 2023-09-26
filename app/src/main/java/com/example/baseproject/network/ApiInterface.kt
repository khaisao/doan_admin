package com.example.baseproject.network

import com.example.baseproject.model.AccountStudentResponse
import com.example.baseproject.model.AccountTeacherResponse
import com.example.baseproject.model.AllImageProfileStudentForCourse
import com.example.baseproject.model.ApiObjectResponse
import com.example.baseproject.model.CourseHaveShedule
import com.example.baseproject.model.ImageProfileResponse
import com.example.baseproject.model.LoginResponse
import com.example.baseproject.model.RegisterAccountRequest
import com.example.baseproject.model.AttendanceBody
import okhttp3.MultipartBody

import okhttp3.RequestBody
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST("api/login")
    suspend fun login(
        @Field("userName") userName: String,
        @Field("password") password: String,
    ): ApiObjectResponse<LoginResponse>

    @GET("api/teacher/getAllImageProfileStudentForCourse/{id}")
    suspend fun getAllImageProfileStudentForCourse(
        @Path("id") id: Int = 0
    ): ApiObjectResponse<List<AllImageProfileStudentForCourse>>

    @POST("api/admin/register")
    suspend fun registerAccount(
        @Body requestAccountRequest: RegisterAccountRequest
    ): ApiObjectResponse<Any>

    @GET("api/admin/getAllAccountTeacher")
    suspend fun getAllAccountTeacher(
    ): ApiObjectResponse<List<AccountTeacherResponse>>

    @GET("api/admin/getAllAccountStudent")
    suspend fun getAllAccountStudent(
    ): ApiObjectResponse<List<AccountStudentResponse>>

    @GET("api/admin/getAllCourseHaveSchedule")
    suspend fun getAllCourseHaveSchedule(
    ): ApiObjectResponse<List<CourseHaveShedule>>

    @Multipart
    @POST("api/student/updateImageProfile")
    suspend fun updateImageProfile(
        @Part("studentId") studentId: RequestBody, @Part part: MultipartBody.Part
    ): ApiObjectResponse<Any>

    @GET("api/student/getImageProfile/{id}")
    suspend fun getImageProfile(
        @Path("id") id: Int = 0
    ): ApiObjectResponse<ImageProfileResponse>

    @POST("api/teacher/attendance")
    suspend fun attendance(
        @Body attendanceBody: AttendanceBody
    ): ApiObjectResponse<Any>

}
