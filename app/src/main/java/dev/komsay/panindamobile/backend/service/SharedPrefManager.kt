package dev.komsay.panindamobile.backend.service

import android.content.Context
import android.content.SharedPreferences

object SharedPrefManager {
    private const val PREF_NAME = "paninda_pref"
    private const val KEY_TOKEN = "jwt_token"
    private const val KEY_USERNAME = "username"

    fun saveToken(context: Context, token: String): Boolean {
        val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.edit().putString(KEY_TOKEN, token).commit()
    }

    fun getToken(context: Context): String? {
        val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(KEY_TOKEN, null)
    }

    fun saveUsername(context: Context, username: String): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.edit().putString(KEY_USERNAME, username).commit()
    }

    fun getUsername(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_USERNAME, null) // Returns the username, or null if not found
    }

    fun saveLoginData(context: Context, token: String, username: String): Boolean {
        val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_USERNAME, username)
            .commit()
    }

    fun clear(context: Context) {
        val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        return getToken(context) != null
    }
}
