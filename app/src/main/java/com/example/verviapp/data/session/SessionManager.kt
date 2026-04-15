package com.example.verviapp.data.session

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val prefs: SharedPreferences
) {

    fun saveUserSession(userId: Int) {
        prefs.edit().putInt(KEY_USER_ID, userId).apply()
    }

    fun clearSession() {
        prefs.edit().remove(KEY_USER_ID).apply()
    }

    fun getLoggedInUserId(): Int? {
        if (!prefs.contains(KEY_USER_ID)) return null
        return prefs.getInt(KEY_USER_ID, -1).takeIf { it > 0 }
    }

    fun isLoggedIn(): Boolean = getLoggedInUserId() != null

    private companion object {
        const val KEY_USER_ID = "session_user_id"
    }
}
