package com.example.baseproject.util

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

fun ViewGroup.scrollDownTo(descendant: View) {
    // Could use smoothScrollBy, but it sometimes over-scrolled a lot
    howFarDownIs(descendant)?.let { scrollBy(0, it) }
}

/**
 * Calculate how many pixels below the visible portion of this [ViewGroup] is the
 * bottom of [descendant].
 *
 * In other words, how much you need to scroll down, to make [descendant]'s bottom
 * visible.
 */
fun ViewGroup.howFarDownIs(descendant: View): Int? {
    val bottom = Rect().also {
        // See https://stackoverflow.com/a/36740277/1916449
        descendant.getDrawingRect(it)
        offsetDescendantRectToMyCoords(descendant, it)
    }.bottom
    return (bottom - height - scrollY).takeIf { it > 0 }
}

@RequiresApi(Build.VERSION_CODES.M)
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
    return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

fun Fragment.dismissAllDialog() {
    childFragmentManager.fragments.let { fragments ->
        if (fragments.isNotEmpty())
            for (fragment in fragments) {
                when (fragment) {
                    is DialogFragment -> fragment.dismiss()
                    is Dialog -> fragment.dismiss()
                    else -> {}
                }
            }
    }
}

fun String.toPayLength() = this.replace("\n", "").replace("\r", "").trim().length

fun String.toPayPoint(pointPerChar: Int) =
    pointPerChar * (((this.toPayLength() - 1) / 10) + 1)

inline fun <reified T> Any.toListData(): List<T> {
    return if (this is List<*>) {
        val gson = Gson()
        gson.fromJson<List<T>>(
            gson.toJson(this),
            TypeToken.getParameterized(List::class.java, T::class.java).type
        ) ?: emptyList()
    } else {
        emptyList()
    }
}
