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
        val PREF_PARAM_ACCESS_TOKEN = stringPreferencesKey("PREF_PARAM_ACCESS_TOKEN")
        val PREF_PARAM_LANGUAGE = stringPreferencesKey("PREF_PARAM_LANGUAGE")
        const val PREF_PARAM_PASSWORD = "PREF_PARAM_PASSWORD"
        const val PREF_PARAM_EMAIL_USER = "PREF_PARAM_EMAIL_USER"

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

    override fun getToken(): Flow<String?> = getValue(PREF_PARAM_ACCESS_TOKEN)

    override suspend fun setUserToken(userToken: String) {
        putValue(PREF_PARAM_ACCESS_TOKEN, userToken)
    }

    override fun getLanguage(): Flow<String?> = getValue(PREF_PARAM_LANGUAGE)

    override suspend fun setLanguage(language: String) {
        putValue(PREF_PARAM_LANGUAGE, language)
    }

    override fun logout() {

    }

    override fun saveEmail(email: String) {
        put(PREF_PARAM_EMAIL_USER, email)
    }

    override fun getEmail(): String? = get(PREF_PARAM_EMAIL_USER)

    override fun savePassword(password: String) {
        put(PREF_PARAM_PASSWORD, password)
    }

    override fun getPassword(): String? = get(PREF_PARAM_PASSWORD)

}