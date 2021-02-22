package com.example.lyceum_application_android_client

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager


object Utils {
    fun hideKeyboard(activity: Activity) { // Check if no view has focus:
        val view = activity.currentFocus
        if (view != null) {
            val inputManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}