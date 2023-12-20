package com.khaipv.attendance.navigation

import android.os.Bundle
import com.example.core.navigationComponent.BaseNavigator

interface AppNavigation : BaseNavigator {

    fun openSplashToLoginScreen(bundle: Bundle? = null)

    fun openLoginToAdminTop(bundle: Bundle? = null)

    fun openSplashToAdminTop(bundle: Bundle? = null)

    fun openLoginToTeacherTop(bundle: Bundle? = null)

    fun openLoginToStudentTop(bundle: Bundle? = null)

    fun openSplashToTeacherTop(bundle: Bundle? = null)

    fun openSplashToStudentTop(bundle: Bundle? = null)

    fun openLoginScreenAndClearBackStack(bundle: Bundle? = null)

    fun openAdminProfileToChangePassword(bundle: Bundle? = null)

    fun openStudentProfileToChangePassword(bundle: Bundle? = null)

    fun openTeacherProfileToChangePassword(bundle: Bundle? = null)

    fun openAdminToChangePasswordSuccess(bundle: Bundle? = null)

    fun openStudentToChangePasswordSuccess(bundle: Bundle? = null)

    fun openTeacherToChangePasswordSuccess(bundle: Bundle? = null)

    fun openHomeToAddAccount(bundle: Bundle? = null)

    fun openAddAccountToAddAccountSuccess(bundle: Bundle? = null)

    fun openHomeToUserProfile(bundle: Bundle? = null)

    fun openScheduleToDetailCourse(bundle: Bundle? = null)

    fun openScheduleCourseToFaceRecoFaceNet(bundle: Bundle? = null)

    fun openScheduleCourseToFaceRecoKbi(bundle: Bundle? = null)

    fun openScheduleCourseToHistoryAttendance(bundle: Bundle? = null)

    fun openFaceRecoFaceNetToListFaceReco(bundle: Bundle? = null)

    fun openFaceRecoKbiToListFaceReco(bundle: Bundle? = null)

    fun openStudentTopToDetailScheduleStudent(bundle: Bundle? = null)

    fun openDetailCourseToAllAttendance(bundle: Bundle? = null)

    fun openToFaceScan(bundle: Bundle? = null)

    fun openFaceScanToFaceScanSuccess(bundle: Bundle? = null)

    fun openAdminTopToSchedule(bundle: Bundle? = null)

    fun openScheduleTeacherToEditSchedule(bundle: Bundle? = null)

    fun openEditScheduleToEditScheduleSuccess(bundle: Bundle? = null)

    fun openGlobalSplashToDetailAttendance(bundle: Bundle? = null)

    fun openListFaceRecoToAttendanceSuccess(bundle: Bundle? = null)
}