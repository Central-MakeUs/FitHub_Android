package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseAlarmData (
    val result : ResponseAlarmList
) : BaseResponse() {
    data class ResponseAlarmList(
        val alarmList : List<ResultAlarmData>
    )
    data class ResultAlarmData(
        val alarmType : String,
        val alarmBody : String,
        val targetId : Int,
        val alarmId : Int,
        val isConfirmed : Boolean,
        val createdAt : String
    )
}