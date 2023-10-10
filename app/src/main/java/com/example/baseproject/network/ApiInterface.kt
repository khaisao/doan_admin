package com.example.baseproject.network

import com.example.baseproject.model.AccountStudentResponse
import com.example.baseproject.model.AccountTeacherResponse
import com.example.baseproject.model.AddImageProfileBody
import com.example.baseproject.model.AllImageProfileStudentForCourse
import com.example.baseproject.model.ApiObjectResponse
import com.example.baseproject.model.CourseHaveShedule
import com.example.baseproject.model.DataImageProfileResponse
import com.example.baseproject.model.LoginResponse
import com.example.baseproject.model.RegisterAccountRequest
import com.example.baseproject.model.AttendanceBody
import com.example.baseproject.model.CourseStudentRegister
import com.example.baseproject.model.CourseTeacherAssign
import com.example.baseproject.model.DetailScheduleCourse
import com.example.baseproject.model.DetailScheduleStudent
import com.example.baseproject.model.OverviewScheduleStudent
import com.example.baseproject.model.TeacherInfoResponse
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

    @Multipart
    @PATCH("api/teacher/updateAvatar")
    suspend fun updateTeacherAvatar(
        @Part("teacherId") teacherId: RequestBody, @Part part: MultipartBody.Part
    ): ApiObjectResponse<Any>

    @GET("api/student/getDataImageProfile/{id}")
    suspend fun getDataImageProfile(
        @Path("id") id: Int = 0
    ): ApiObjectResponse<DataImageProfileResponse>

    @GET("api/student/getAllCourseRegister/{studentId}")
    suspend fun getAllCourseRegister(
        @Path("studentId") id: Int
    ): ApiObjectResponse<List<CourseStudentRegister>>

    @GET("api/teacher/getAllCourseAssign/{teacherId}")
    suspend fun getAllCourseAssign(
        @Path("teacherId") id: Int
    ): ApiObjectResponse<List<CourseTeacherAssign>>

    @POST("api/teacher/attendance")
    suspend fun attendance(
        @Body attendanceBody: AttendanceBody
    ): ApiObjectResponse<Any>

    @FormUrlEncoded
    @PATCH("api/changePassword")
    suspend fun changePassword(
        @Field("accountId") accountId: Int,
        @Field("password") password: String,
    ): ApiObjectResponse<Any>

    @GET("api/student/getDetailScheduleStudent/{studentId}/{coursePerCycleId}")
    suspend fun getDetailScheduleStudent(
        @Path("studentId") studentId: Int,
        @Path("coursePerCycleId") coursePerCycleId: Int,
    ): ApiObjectResponse<List<DetailScheduleStudent>>

    @GET("api/teacher/getAllAttendanceSpecificCourse/{courseId}")
    suspend fun getAllAttendanceSpecificCourse(
        @Path("courseId") courseId: Int,
    ): ApiObjectResponse<List<OverviewScheduleStudent>>

    @GET("api/teacher/getAllScheduleSpecificCourse/{courseId}")
    suspend fun getAllScheduleSpecificCourse(
        @Path("courseId") courseId: Int,
    ): ApiObjectResponse<List<DetailScheduleCourse>>

    @POST("api/student/addImageProfile")
    suspend fun addImageProfile(
        @Body addImageProfileBody: AddImageProfileBody
    ): ApiObjectResponse<Any>

    @GET("api/teacher/getTeacherInfo/{teacherId}")
    suspend fun getTeacherInfo(
        @Path("teacherId") teacherId: Int,
    ): ApiObjectResponse<TeacherInfoResponse>

}
