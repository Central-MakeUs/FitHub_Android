package com.proteam.fithub.data.remote.request

data class RequestChangePassword(
    val targetPhoneNum : String,
    val newPassword : String
)
