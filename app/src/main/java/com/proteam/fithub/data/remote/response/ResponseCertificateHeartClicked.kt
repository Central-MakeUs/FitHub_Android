package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseCertificateHeartClicked(
    val result : ResultCertificateHeartClicked
) : BaseResponse() {
    data class ResultCertificateHeartClicked(
        val recordId : Int,
        val newLikes : Int
    )
}
