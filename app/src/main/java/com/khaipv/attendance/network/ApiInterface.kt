package com.khaipv.attendance.network

import com.khaipv.attendance.model.AccountStudentResponse
import com.khaipv.attendance.model.AccountTeacherResponse
import com.khaipv.attendance.model.AddImageProfileBody
import com.khaipv.attendance.model.AllImageProfileStudentForCourse
import com.khaipv.attendance.model.ApiObjectResponse
import com.khaipv.attendance.model.AttendanceBody
import com.khaipv.attendance.model.Classroom
import com.khaipv.attendance.model.DataImageProfileResponse
import com.khaipv.attendance.model.DetailAttendanceStudent
import com.khaipv.attendance.model.DetailAttendanceStudentTeacherScreen
import com.khaipv.attendance.model.DetailScheduleCourse
import com.khaipv.attendance.model.LoginResponse
import com.khaipv.attendance.model.OverViewCourseHaveShedule
import com.khaipv.attendance.model.OverviewCourseStudentRegister
import com.khaipv.attendance.model.OverviewCourseTeacherAssign
import com.khaipv.attendance.model.OverviewScheduleStudent
import com.khaipv.attendance.model.RegisterAccountRequest
import com.khaipv.attendance.model.StudentInfoResponse
import com.khaipv.attendance.model.TeacherInfoResponse
import com.khaipv.attendance.model.UpdateScheduleBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST("api/login")
    suspend fun login(
        @Field("userName") userName: String,
        @Field("password") password: String,
        @Field("fcmDeviceToken") fcmDeviceToken: String,
    ): ApiObjectResponse<LoginResponse>

    @GET("api/teacher/getAllImageProfileStudentForCourse/{id}/{modelMode}")
    suspend fun getAllImageProfileStudentForCourse(
        @Path("id") id: Int = 0,
        @Part("modelMode") modelMode: Int = 0
    ): ApiObjectResponse<List<AllImageProfileStudentForCourse>>

    @GET("api/teacher/getAllImageProfileStudentForCourse/{id}/1")
    suspend fun getAllImageKbiProfileStudentForCourse(
        @Path("id") id: Int = 0,
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
    ): ApiObjectResponse<List<OverViewCourseHaveShedule>>

    @Multipart
    @PATCH("api/teacher/updateAvatar")
    suspend fun updateTeacherAvatar(
        @Part("teacherId") teacherId: RequestBody, @Part part: MultipartBody.Part
    ): ApiObjectResponse<Any>

    @Multipart
    @PATCH("api/student/updateAvatar")
    suspend fun updateStudentAvatar(
        @Part("studentId") studentId: RequestBody, @Part part: MultipartBody.Part
    ): ApiObjectResponse<Any>

    @GET("api/student/getDataImageProfile/{id}")
    suspend fun getDataImageProfile(
        @Path("id") id: Int = 0
    ): ApiObjectResponse<DataImageProfileResponse>

    @GET("api/student/getAllCourseRegister/{studentId}")
    suspend fun getAllCourseRegister(
        @Path("studentId") id: Int
    ): ApiObjectResponse<List<OverviewCourseStudentRegister>>

    @GET("api/teacher/getAllCourseAssign/{teacherId}")
    suspend fun getAllCourseAssign(
        @Path("teacherId") id: Int
    ): ApiObjectResponse<List<OverviewCourseTeacherAssign>>

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
    ): ApiObjectResponse<List<DetailAttendanceStudent>>

    @GET("api/teacher/getAllAttendanceSpecificCourse/{coursePerCycleId}")
    suspend fun getAllAttendanceSpecificCourse(
        @Path("coursePerCycleId") coursePerCycleId: Int,
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

    @GET("api/student/getStudentInfo/{studentId}")
    suspend fun getStudentInfo(
        @Path("studentId") studentId: Int,
    ): ApiObjectResponse<StudentInfoResponse>

    @GET("api/teacher/getAllAttendanceSpecificSchedule/{coursePerCycleId}/{scheduleId}")
    suspend fun getAllAttendanceSpecificSchedule(
        @Path("coursePerCycleId") coursePerCycleId: Int,
        @Path("scheduleId") scheduleId: Int,
    ): ApiObjectResponse<List<DetailAttendanceStudentTeacherScreen>>

    @GET("api/teacher/getAllClassroom")
    suspend fun getAllClassroom(
    ): ApiObjectResponse<List<Classroom>>

    @PATCH("api/teacher/updateSchedule")
    suspend fun updateSchedule(
        @Body updateScheduleBody: UpdateScheduleBody
    ): ApiObjectResponse<Any>

}
