package com.example.core.pref

import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
interface RxPreferences : BasePreferences {

    fun put(key: String, value: String)

    fun get(key: String): String?

    fun remove(key: String)

    fun getToken(): Flow<String?>

    suspend fun setUserToken(userToken: String)

    fun getLanguage(): Flow<String?>

    suspend fun setLanguage(language: String)

    fun logout()

    fun saveEmail(email: String)

    fun getEmail(): String?

    fun savePassword(password: String)

    fun getPassword(): String?

}