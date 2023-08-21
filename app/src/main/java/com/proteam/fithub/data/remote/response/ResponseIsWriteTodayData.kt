package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseIsWriteTodayData(
    val result : ResultIsWriteTodayData
) : BaseResponse() {
    data class ResultIsWriteTodayData(
        val isWrite : Boolean
    )
}
