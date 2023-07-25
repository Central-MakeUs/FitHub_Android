package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseSignUpWithSocial(
    val result : ResultSignUpWithSocial
) : BaseResponse() {
    data class ResultSignUpWithSocial(
        val userId : Int,
        val nickname : String,
        val accessToken : String
    )
}
