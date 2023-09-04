package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseTermsData (
    val result : ResultTermsData
) : BaseResponse() {
    data class ResultTermsData(
        val id : Int,
        val link : String
    )
}