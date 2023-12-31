package com.example.core.pref

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.core.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext context: Context
) : BasePreferencesImpl(context), RxPreferences {

    companion object{
        const val PREF_PARAM_TOKEN = "PREF_PARAM_TOKEN"
        val PREF_PARAM_LANGUAGE = stringPreferencesKey("PREF_PARAM_LANGUAGE")
        const val PREF_PARAM_PASSWORD = "PREF_PARAM_PASSWORD"
        const val PREF_PARAM_USERNAME_LOGIN = "PREF_PARAM_USERNAME_LOGIN"
        const val PREF_PARAM_ROLE = "PREF_PARAM_ROLE"
        const val PREF_PARAM_AVATAR = "PREF_PARAM_AVATAR"
        const val PREF_PARAM_NAME = "PREF_PARAM_NAME"
        const val PREF_STUDENT_ID = "PREF_STUDENT_ID"
        const val PREF_TEACHER_ID = "PREF_TEACHER_ID"
        const val PREF_ADMIN_ID = "PREF_ADMIN_ID"
        const val PREF_ACCOUNT_ID = "PREF_ACCOUNT_ID"

    }

    private val mPrefs: SharedPreferences = context.getSharedPreferences(
        Constants.PREF_FILE_NAME,
        Context.MODE_PRIVATE
    )

    override fun put(key: String, value: String) {
        val editor: SharedPreferences.Editor = mPrefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    override fun get(key: String): String? {
        return mPrefs.getString(key, "")
    }

    override fun remove(key: String) {
        val editor: SharedPreferences.Editor = mPrefs.edit()
        editor.remove(key)
        editor.apply()
    }

    override fun getLanguage(): Flow<String?> = getValue(PREF_PARAM_LANGUAGE)

    override suspend fun setLanguage(language: String) {
        putValue(PREF_PARAM_LANGUAGE, language)
    }

    override fun logout() {
        // Add this if need
    }

    override fun saveUserName(userName: String) {
        put(PREF_PARAM_USERNAME_LOGIN, userName)
    }

    override fun getUserName(): String? = get(PREF_PARAM_USERNAME_LOGIN)

    override fun savePassword(password: String) {
        put(PREF_PARAM_PASSWORD, password)
    }

    override fun getPassword(): String? = get(PREF_PARAM_PASSWORD)
    override fun saveToken(token: String) {
        put(PREF_PARAM_TOKEN, token)
    }

    override fun getToken(): String? = get(PREF_PARAM_TOKEN)
    override fun saveRole(role: Int) {
        mPrefs.edit().apply {
            putInt(PREF_PARAM_ROLE, role)
        }.also { it.apply() }
    }

    override fun getRole(): Int {
        return mPrefs.getInt(PREF_PARAM_ROLE, 0)

    }

    override fun saveName(name: String) {
        mPrefs.edit().apply {
            putString(PREF_PARAM_NAME, name)
        }.also { it.apply() }
    }

    override fun getName(): String? {
        return mPrefs.getString(PREF_PARAM_NAME, "")
    }

    override fun saveAvatar(avatar: String) {
        mPrefs.edit().apply {
            putString(PREF_PARAM_AVATAR, avatar)
        }.also { it.apply() }    }

    override fun getAvatar(): String? {
        return mPrefs.getString(PREF_PARAM_AVATAR, "")
    }

    override fun saveStudentId(studentId: Int) {
        mPrefs.edit().apply {
            putInt(PREF_STUDENT_ID, studentId)
        }.also { it.apply() }       }

    override fun getStudentId(): Int {
        return mPrefs.getInt(PREF_STUDENT_ID, 0)
    }

    override fun saveTeacherId(teacherId: Int) {
        mPrefs.edit().apply {
            putInt(PREF_TEACHER_ID, teacherId)
        }.also { it.apply() }
    }

    override fun getTeacherId(): Int {
        return mPrefs.getInt(PREF_TEACHER_ID, 0)
    }

    override fun saveAdminId(adminId: Int) {
        mPrefs.edit().apply {
            putInt(PREF_ADMIN_ID, adminId)
        }.also { it.apply() }
    }

    override fun getAdminId(): Int {
        return mPrefs.getInt(PREF_ADMIN_ID, 0)
    }

    override fun saveAccountId(accountId: Int) {
        mPrefs.edit().apply {
            putInt(PREF_ACCOUNT_ID, accountId)
        }.also { it.apply() }           }

    override fun getAccountId(): Int {
        return mPrefs.getInt(PREF_ACCOUNT_ID, 0)
    }

}