package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseCertificateData (
    val result : ListResultCertificateData
) : BaseResponse() {
    data class ListResultCertificateData(
        val recordList : List<ResultCertificateData>,
        val totalPage : Int
    )
    data class ResultCertificateData(
       val recordId : Int,
       val pictureUrl : String,
       var likes : Int,
       var isLiked : Boolean,
       val createdAt : String
    )
}