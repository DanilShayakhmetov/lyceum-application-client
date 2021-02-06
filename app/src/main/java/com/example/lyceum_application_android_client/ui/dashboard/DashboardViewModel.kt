package com.example.lyceum_application_android_client.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = ""
    }
    val text: LiveData<String> = _text

    private val _monday = MutableLiveData<String>().apply {
        value = ""
    }
    val monday: LiveData<String> = _monday

    private val _tuesday = MutableLiveData<String>().apply {
        value = ""
    }
    val tuesday: LiveData<String> = _tuesday

    private val _wednesday = MutableLiveData<String>().apply {
        value = ""
    }
    val wednesday: LiveData<String> = _wednesday

    private val _thursday = MutableLiveData<String>().apply {
        value = ""
    }
    val thursday: LiveData<String> = _thursday

    private val _friday = MutableLiveData<String>().apply {
        value = ""
    }
    val friday: LiveData<String> = _friday

}