package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseSignIn(
    val result : ResultSignIn
) : BaseResponse() {

    data class ResultSignIn(
        val jwt : String
    )
}
