package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseAlarmAvailableData (
    val result : ResultAlarmAvailableData
) : BaseResponse() {
    data class ResultAlarmAvailableData(
        val isRemain : Boolean
    )
}