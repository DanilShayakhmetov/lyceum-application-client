package com.example.lyceum_application_android_client

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.lyceum_application_android_client.MainActivity
import java.util.*

class SessionManager @SuppressLint("CommitPrefEdits") constructor(// Context
    var _context: Context
) {
    // Shared Preferences
    var pref: SharedPreferences
    // Editor for Shared preferences
    var editor: SharedPreferences.Editor
    // Shared pref mode
    var PRIVATE_MODE = 0

    /**
     * Create login session
     */
    fun createLoginSession(name: String?, email: String?) { // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true)
        // Storing name in pref
        editor.putString(KEY_NAME, name)
        // Storing email in pref
        editor.putString(KEY_EMAIL, email)
        // commit changes
        editor.commit()
    }

    /**
     * Check login method wil check user login status If false it will redirect
     * user to login page Else won't do anything
     */
    fun checkLogin() { // Check login status
        if (!isLoggedIn) { // user is not logged in redirect him to Login Activity
            val i = Intent(_context, MainActivity::class.java)
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            // Add new Flag to start new Activity
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            // Staring Login Activity
            _context.startActivity(i)
        }
    }// user name
    // user email id
    // return user

    /**
     * Get stored session data
     */
    val userDetails: HashMap<String, String?>
        get() {
            val user = HashMap<String, String?>()
            // user name
            user[KEY_NAME] = pref.getString(KEY_NAME, null)
            // user email id
            user[KEY_EMAIL] = pref.getString(KEY_EMAIL, null)
            // return user
            return user
        }

    /**
     * Clear session details
     */
    fun logoutUser() { // Clearing all data from Shared Preferences
        editor.clear()
        editor.commit()
        // After logout redirect user to Loing Activity
        val i = Intent(_context, MainActivity::class.java)
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // Add new Flag to start new Activity
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        // Staring Login Activity
        _context.startActivity(i)
    }

    /**
     * Quick check for login
     */
// Get Login State
    val isLoggedIn: Boolean
        get() = pref.getBoolean(IS_LOGIN, false)

    companion object {
        // Sharedpref file name
        private const val PREF_NAME = "AndroidHivePref"
        // All Shared Preferences Keys
        private const val IS_LOGIN = "IsLoggedIn"
        // User name (make variable public to access from outside)
        const val KEY_NAME = "name"
        // Email address (make variable public to access from outside)
        const val KEY_EMAIL = "email"
    }

    // Constructor
    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }
}