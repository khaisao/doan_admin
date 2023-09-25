package com.example.core.pref

import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
interface RxPreferences : BasePreferences {

    fun put(key: String, value: String)

    fun get(key: String): String?

    fun remove(key: String)

    fun getLanguage(): Flow<String?>

    suspend fun setLanguage(language: String)

    fun logout()

    fun saveUserName(userName: String)

    fun getUserName(): String?

    fun savePassword(password: String)

    fun getPassword(): String?

    fun saveToken(token: String)

    fun getToken(): String?

    fun saveRole(role: Int)

    fun getRole(): Int

    fun saveName(name: String)

    fun getName(): String?

    fun saveStudentId(studentId:Int)

    fun getStudentId(): Int

}