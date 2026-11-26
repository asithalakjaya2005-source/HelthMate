package com.example.healthmate.utils

import android.content.Context

class SessionManager(context: Context) {

    private val preferences =
        context.applicationContext.getSharedPreferences(
            "healthmate_session",
            Context.MODE_PRIVATE
        )

    companion object {
        private const val KEY_USER_ID = "logged_in_user_id"
    }

    fun saveSession(userId: Int) {
        preferences.edit()
            .putInt(KEY_USER_ID, userId)
            .apply()
    }

    fun getUserId(): Int? {

        val userId = preferences.getInt(KEY_USER_ID, -1)

        return if (userId > 0) {
            userId
        } else {
            null
        }
    }

    fun isLoggedIn(): Boolean {
        return getUserId() != null
    }

    fun clearSession() {
        preferences.edit()
            .remove(KEY_USER_ID)
            .apply()
    }
}