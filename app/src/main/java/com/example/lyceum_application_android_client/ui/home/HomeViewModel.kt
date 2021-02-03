package com.example.lyceum_application_android_client.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    val _text = MutableLiveData<String>().apply {
        value = ""
    }
    val text: LiveData<String> = _text

    val _name = MutableLiveData<String>().apply {
        value = ""
    }
    val name: LiveData<String> = _name

    val _first = MutableLiveData<String>().apply {
        value = ""
    }
    val first: LiveData<String> = _first

    val _last = MutableLiveData<String>().apply {
        value = ""
    }

    val last: LiveData<String> = _last

    val _middle = MutableLiveData<String>().apply {
        value = ""
    }

    val middle: LiveData<String> = _middle


    val _email = MutableLiveData<String>().apply {
        value = ""
    }
    val email: LiveData<String> = _email

    val _classId = MutableLiveData<String>().apply {
        value = ""
    }

    val classId: LiveData<String> = _classId


}