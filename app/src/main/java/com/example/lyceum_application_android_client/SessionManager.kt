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
    fun createLoginSession(name: String?, id: String?, schedule1: String?, schedule2: String?, schedule3: String?, schedule4: String?, schedule5: String?, class_id: String?) { // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true)
        // Storing name in pref
        editor.putString(KEY_NAME, name)
        editor.putString(KEY_SCHEDULE_1, schedule1)
        editor.putString(KEY_SCHEDULE_2, schedule2)
        editor.putString(KEY_SCHEDULE_3, schedule3)
        editor.putString(KEY_SCHEDULE_4, schedule4)
        editor.putString(KEY_SCHEDULE_5, schedule5)
        editor.putString(KEY_CLASS_ID, class_id)
        editor.putString(KEY_ID, id)
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
            user[KEY_SCHEDULE_1] = pref.getString(KEY_SCHEDULE_1, null)
            user[KEY_SCHEDULE_2] = pref.getString(KEY_SCHEDULE_2, null)
            user[KEY_SCHEDULE_3] = pref.getString(KEY_SCHEDULE_3, null)
            user[KEY_SCHEDULE_4] = pref.getString(KEY_SCHEDULE_4, null)
            user[KEY_SCHEDULE_5] = pref.getString(KEY_SCHEDULE_5, null)
            user[KEY_CLASS_ID] = pref.getString(KEY_CLASS_ID, null)
            user[KEY_ID] = pref.getString(KEY_ID, null)
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
        const val KEY_ID = "id"
        const val KEY_SCHEDULE_1 = "schedule1"
        const val KEY_SCHEDULE_2 = "schedule2"
        const val KEY_SCHEDULE_3 = "schedule3"
        const val KEY_SCHEDULE_4 = "schedule4"
        const val KEY_SCHEDULE_5 = "schedule5"
        const val KEY_CLASS_ID = "class_id"
    }

    // Constructor
    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }
}