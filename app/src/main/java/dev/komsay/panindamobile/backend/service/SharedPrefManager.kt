package dev.komsay.panindamobile.backend.service

import android.content.Context
import android.content.SharedPreferences

object SharedPrefManager {
    private const val PREF_NAME = "paninda_pref"
    private const val KEY_TOKEN = "jwt_token"
    private const val KEY_NEW_TOKEN = "new_token"
    private const val KEY_USERNAME = "username"

    fun saveToken(context: Context, token: String): Boolean {

        val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.edit().putString(KEY_TOKEN, token).commit()
    }

    fun getToken(context: Context): String? {
        val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        return sharedPref.getString(KEY_TOKEN, null)
    }

    fun saveNewToken(context: Context, token: String) {
        val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPref.edit().putString(KEY_NEW_TOKEN, token).apply()
    }

    fun getNewToken(context: Context): String? {
        val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(KEY_NEW_TOKEN, null)
    }

    fun clear(context: Context) {
        val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()
    }

    fun saveUsername(context: Context, username: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, username)
        editor.apply()
    }

    fun getUsername(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_USERNAME, null) // Returns the username, or null if not found
    }

}
