package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponsePostCertificateData (
    val result : ResultPostCertificateData
) : BaseResponse() {
    data class ResultPostCertificateData(
        val recordId : Int,
        val ownerId : Int,
        val createdAt : String
    )
}