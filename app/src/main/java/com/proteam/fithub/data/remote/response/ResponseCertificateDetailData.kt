package com.proteam.fithub.data.remote.response

import com.proteam.fithub.data.data.CategoryData
import com.proteam.fithub.data.data.ComponentUserData
import com.proteam.fithub.data.data.HashTagData
import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseCertificateDetailData(
    val result : ResultCertificateDetailData
) : BaseResponse() {
    data class ResultCertificateDetailData(
        val recordId : Int,
        val recordCategory : CategoryData,
        val userInfo : ComponentUserData,
        val contents : String,
        val pictureImage : String,
        val createdAt : String,
        var likes : Int,
        var isLiked : Boolean,
        val hashtags : HashTagData
    )
}
