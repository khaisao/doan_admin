package com.example.core.navigationComponent

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.core.R
import timber.log.Timber
import java.lang.ref.WeakReference

val defaultNavOptions = NavOptions.Builder()
    .setEnterAnim(R.anim.slide_in_left)
    .setExitAnim(R.anim.slide_out_right)
    .setPopEnterAnim(R.anim.slide_in_right)
    .setPopExitAnim(R.anim.slide_out_left)
    .build()

val nullNavOptions = NavOptions.Builder()
    .setEnterAnim(R.anim.null_anim)
    .setExitAnim(R.anim.null_anim)
    .setPopEnterAnim(R.anim.null_anim)
    .setPopExitAnim(R.anim.null_anim)
    .build()

abstract class BaseNavigatorImpl : BaseNavigator {

    override var navController: NavController? = null

    override fun bind(navController: NavController) {
        this.navController = WeakReference(navController).get()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            run {
                Timber.tag("Back stack Navigation").d(destination.navigatorName)
            }
        }
    }

    override fun unbind() {
        navController = null
    }

    override fun openScreen(
        @IdRes id: Int,
        bundle: Bundle?
    ) {
        navController?.navigate(id, bundle)
    }

    override fun navigateUp(): Boolean? {
        return navController?.navigateUp()
    }

    override fun currentFragmentId(): Int? {
        return navController?.currentDestination?.id
    }

    override fun setStartDestination(@IdRes id: Int) {
        if (navController == null) {
            return
        }
        val navGraph = navController?.graph
        navGraph?.setStartDestination(id)
        if (navGraph != null) {
            navController?.graph = navGraph
        }
    }
}