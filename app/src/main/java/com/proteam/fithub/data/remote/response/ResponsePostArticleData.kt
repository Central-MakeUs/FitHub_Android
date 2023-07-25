package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponsePostArticleData (
    val result : ResultPostCertificateData
) : BaseResponse() {
    data class ResultPostCertificateData(
        val articleId : Int,
        val title : String,
        val ownerId : Int,
        val createdAt : String
    )
}