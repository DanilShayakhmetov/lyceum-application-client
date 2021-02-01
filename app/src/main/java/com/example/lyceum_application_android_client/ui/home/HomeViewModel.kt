package com.example.lyceum_application_android_client.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lyceum_application_android_client.DatabaseHelper
import com.example.lyceum_application_android_client.SessionManager
import com.example.lyceum_application_android_client.models.Users

class HomeViewModel : ViewModel() {

    val _text = MutableLiveData<String>().apply {
        value = ""
    }
    val text: LiveData<String> = _text

}