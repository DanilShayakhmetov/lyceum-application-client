package com.example.lyceum_application_android_client.models

data class User(
    var userName: String = "",
    var email: String = "",
    var password: String = "",
    var firstName: String = "",
    val classId: Int = 1,
    var lastName: String = "",
    var middleName: String = ""
)