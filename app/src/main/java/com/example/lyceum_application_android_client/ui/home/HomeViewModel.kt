package com.example.lyceum_application_android_client.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lyceum_application_android_client.DatabaseHelper

class HomeViewModel : ViewModel() {

    lateinit var handler: DatabaseHelper

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _personalPage = MutableLiveData<String>().apply {
        value =
    }
    val text: LiveData<String> = _text

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private fun getUserData(name:String) {
        var user = handler.getUserByName()
    }
}