package com.proteam.fithub.data.remote.response

import com.proteam.fithub.data.data.UserDetailData
import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseHomeData (
    val result : ResultHomeData
        ) : BaseResponse() {
            data class ResultHomeData(
                val userInfo : UserDetailData,
                val bestRecorderList : List<RecorderItems>
            )

            data class RecorderItems (
                val ranking : Int,
                val rankingStatus : String,
                val recorderNickName : String,
                val category : String,
                val level : Int,
                val profileUrl : String,
                val recordCount : Int,
                val gradeName : String
                    )
        }