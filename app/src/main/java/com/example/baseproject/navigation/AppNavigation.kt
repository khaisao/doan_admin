package com.example.baseproject.navigation

import android.os.Bundle
import com.example.core.navigationComponent.BaseNavigator

interface AppNavigation : BaseNavigator {

    fun openSplashToLoginScreen(bundle: Bundle? = null)

    fun openLoginToAdminTop(bundle: Bundle? = null)

    fun openSplashToAdminTop(bundle: Bundle? = null)

    fun openLoginToTeacherTop(bundle: Bundle? = null)

    fun openSplashToTeacherTop(bundle: Bundle? = null)

    fun openLoginScreenAndClearBackStack(bundle: Bundle? = null)

    fun openProfileToChangePassword(bundle: Bundle? = null)

    fun openChangePasswordToChangePasswordSuccess(bundle: Bundle? = null)

    fun openHomeToAddAccount(bundle: Bundle? = null)

    fun openAddAccountToAddAccountSuccess(bundle: Bundle? = null)

    fun openHomeToUserProfile(bundle: Bundle? = null)
    fun openToFaceReco(bundle: Bundle? = null)
}