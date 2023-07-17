package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseSignUpWithPhone(
    val result : ResultSignUpWithPhone
) : BaseResponse() {
    data class ResultSignUpWithPhone(
        val userId : Int,
        val nickname : String,
        val accessToken : String
    )
}
