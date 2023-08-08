package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseMyInfoData(
    val result : ResultMyInfoData
) : BaseResponse() {
    data class ResultMyInfoData(
        val name : String,
        val email : String?,
        val phoneNum : String?
    )
}
