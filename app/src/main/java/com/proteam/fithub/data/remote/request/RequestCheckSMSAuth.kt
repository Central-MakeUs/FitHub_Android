package com.proteam.fithub.data.remote.request

data class RequestCheckSMSAuth (
        val phoneNum : String,
        val authNum : Int
        )