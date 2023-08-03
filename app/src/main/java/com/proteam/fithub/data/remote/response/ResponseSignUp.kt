package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseSignUp(
    val result : ResultSignUp
) : BaseResponse() {
    data class ResultSignUp(
        val userId : Int,
        val nickname : String,
        val accessToken : String
    )
}
