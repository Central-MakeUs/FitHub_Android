package com.proteam.fithub.data.remote.request

data class RequestSignUpWithSocial (
    val marketingAgree : Boolean,
    val name : String,
    val nickname : String,
    val birth : String,
    val gender : String,
    val preferExercises : List<Int>
)