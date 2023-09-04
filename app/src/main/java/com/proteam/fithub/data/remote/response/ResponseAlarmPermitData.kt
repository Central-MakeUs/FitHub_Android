package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseAlarmPermitData(
    val result : ResultAlarmPermitData
) : BaseResponse() {
    data class ResultAlarmPermitData(
        val communityPermit : Boolean,
        val marketingPermit : Boolean
    )
}
