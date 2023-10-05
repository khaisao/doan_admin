package com.example.baseproject.navigation

import android.os.Bundle
import com.example.baseproject.R
import com.example.core.navigationComponent.BaseNavigatorImpl
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class AppNavigatorImpl @Inject constructor() : BaseNavigatorImpl(),
    AppNavigation {

    override fun openSplashToLoginScreen(bundle: Bundle?) {
        openScreen(R.id.action_splashFragment_to_loginFragment, bundle)
    }

    override fun openLoginToAdminTop(bundle: Bundle?) {
        openScreen(R.id.action_loginFragment_to_adminHomeFragment, bundle)
    }

    override fun openSplashToAdminTop(bundle: Bundle?) {
        openScreen(R.id.action_splashFragment_to_adminHomeFragment, bundle)
    }

    override fun openLoginToTeacherTop(bundle: Bundle?) {
        openScreen(R.id.action_loginFragment_to_teacher_navigation, bundle)
    }

    override fun openLoginToStudentTop(bundle: Bundle?) {
        openScreen(R.id.action_loginFragment_to_student_navigation, bundle)
    }

    override fun openSplashToTeacherTop(bundle: Bundle?) {
        openScreen(R.id.action_splashFragment_to_teacher_navigation, bundle)
    }

    override fun openSplashToStudentTop(bundle: Bundle?) {
        openScreen(R.id.action_splashFragment_to_student_navigation, bundle)
    }

    override fun openLoginScreenAndClearBackStack(bundle: Bundle?) {
        openScreen(R.id.action_tabAdminProfile_to_loginFragment, bundle)
    }

    override fun openAdminProfileToChangePassword(bundle: Bundle?) {
        openScreen(R.id.action_adminHomeFragment_to_changePasswordFragment, bundle)
    }

    override fun openStudentProfileToChangePassword(bundle: Bundle?) {
        openScreen(R.id.action_studentTopFragment_to_changePasswordFragment2, bundle)
    }

    override fun openAdminToChangePasswordSuccess(bundle: Bundle?) {
        openScreen(R.id.action_changePasswordFragment_to_changePasswordSuccessFragment, bundle)
    }

    override fun openStudentToChangePasswordSuccess(bundle: Bundle?) {
        openScreen(R.id.action_changePasswordFragment2_to_changePasswordSuccessFragment2, bundle)
    }

    override fun openHomeToAddAccount(bundle: Bundle?) {
        openScreen(R.id.action_adminHomeFragment_to_addAccountFragment, bundle)
    }

    override fun openAddAccountToAddAccountSuccess(bundle: Bundle?) {
        openScreen(R.id.action_addAccountFragment_to_changePasswordSuccessFragment, bundle)
    }

    override fun openHomeToUserProfile(bundle: Bundle?) {
        openScreen(R.id.action_adminHomeFragment_to_userProfileFragment, bundle)
    }

    override fun openScheduleToDetailCourse(bundle: Bundle?) {
        openScreen(R.id.action_teacherTopFragment_to_detailCourseFragment, bundle)
    }

    override fun openDetailCourseToFaceReco(bundle: Bundle?) {
        openScreen(R.id.action_detailCourseFragment_to_faceRecoFragment, bundle)
    }

    override fun openFaceRecoToListFaceReco(bundle: Bundle?) {
        openScreen(R.id.action_faceRecoFragment_to_listFaceRecoFragment, bundle)
    }

    override fun openStudentTopToDetailScheduleStudent(bundle: Bundle?) {
        openScreen(R.id.action_studentTopFragment_to_detailScheduleStudentFragment, bundle)
    }

    override fun openDetailCourseToAllAttendance(bundle: Bundle?) {
        openScreen(R.id.action_detailCourseFragment_to_allAttendanceFragment, bundle)
    }

    override fun openSplashToFaceScan(bundle: Bundle?) {
        openScreen(R.id.action_splashFragment_to_faceScanFragment, bundle)
    }

}