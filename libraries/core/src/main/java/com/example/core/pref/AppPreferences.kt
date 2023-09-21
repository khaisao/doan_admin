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
        const val PREF_PARAM_TOKEN = "PREF_PARAM_EMAIL_USER"
        val PREF_PARAM_LANGUAGE = stringPreferencesKey("PREF_PARAM_LANGUAGE")
        const val PREF_PARAM_PASSWORD = "PREF_PARAM_PASSWORD"
        const val PREF_PARAM_EMAIL_LOGIN = "PREF_PARAM_EMAIL_LOGIN"
        const val PREF_PARAM_ROLE = "PREF_PARAM_ROLE"
        const val PREF_PARAM_USER_NAME = "PREF_PARAM_USER_NAME"

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

    }

    override fun saveEmail(email: String) {
        put(PREF_PARAM_EMAIL_LOGIN, email)
    }

    override fun getEmail(): String? = get(PREF_PARAM_EMAIL_LOGIN)

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

    override fun saveUserName(name: String) {
        mPrefs.edit().apply {
            putString(PREF_PARAM_USER_NAME, name)
        }.also { it.apply() }    }

    override fun getUserName(): String? {
        return mPrefs.getString(PREF_PARAM_USER_NAME, "")
    }

}