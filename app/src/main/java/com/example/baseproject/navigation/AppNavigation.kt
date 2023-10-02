package com.example.baseproject.navigation

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

    fun openAdminToChangePasswordSuccess(bundle: Bundle? = null)

    fun openStudentToChangePasswordSuccess(bundle: Bundle? = null)

    fun openHomeToAddAccount(bundle: Bundle? = null)

    fun openAddAccountToAddAccountSuccess(bundle: Bundle? = null)

    fun openHomeToUserProfile(bundle: Bundle? = null)

    fun openScheduleToDetailCourse(bundle: Bundle? = null)

    fun openDetailCourseToFaceReco(bundle: Bundle? = null)

    fun openFaceRecoToListFaceReco(bundle: Bundle? = null)

    fun openStudentTopToDetailScheduleStudent(bundle: Bundle? = null)

    fun openDetailCourseToAllAttendance(bundle: Bundle? = null)
}