package com.example.core.pref

import javax.inject.Singleton

@Singleton
interface RxPreferences {
    fun put(key: String, value: String)

    fun get(key: String): String?

    fun clear()

    fun remove(key: String)

    fun getToken(): String?

    fun setUserToken(userToken: String)

    fun logout()

}