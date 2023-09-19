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

    override fun openLoginToAdminHome(bundle: Bundle?) {
        openScreen(R.id.action_loginFragment_to_adminHomeFragment, bundle)
    }

    override fun openSplashToAdminHome(bundle: Bundle?) {
        openScreen(R.id.action_splashFragment_to_adminHomeFragment, bundle)
    }

    override fun openLoginScreenAndClearBackStack(bundle: Bundle?) {
        openScreen(R.id.action_tabAdminProfile_to_loginFragment, bundle)
    }

    override fun openProfileToChangePassword(bundle: Bundle?) {
        openScreen(R.id.action_adminHomeFragment_to_changePasswordFragment, bundle)
    }

    override fun openChangePasswordToChangePasswordSuccess(bundle: Bundle?) {
        openScreen(R.id.action_changePasswordFragment_to_changePasswordSuccessFragment, bundle)
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

}