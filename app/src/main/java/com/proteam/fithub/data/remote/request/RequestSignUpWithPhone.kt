package com.proteam.fithub.data.remote.request

data class RequestSignUpWithPhone (
    val marketingAgree : Boolean,
    val phoneNumber : String,
    val name : String,
    val nickname : String,
    val password : String,
    val birth : String,
    val gender : String,
    val preferExercises : List<Int>
)