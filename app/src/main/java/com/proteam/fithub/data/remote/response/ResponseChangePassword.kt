package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseChangePassword(
    val result : ResultChangePassword
) : BaseResponse() {
    data class ResultChangePassword(
        val newPass : String
    )
}
