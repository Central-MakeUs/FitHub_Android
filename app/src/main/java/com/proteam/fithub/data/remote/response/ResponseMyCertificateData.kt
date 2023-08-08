package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseMyCertificateData (
    val result : ListResultMyCertificateData
) : BaseResponse() {
    data class ListResultMyCertificateData(
        val recordList : List<ResultMyCertificateData>,
        val totalPage : Int
    )
    data class ResultMyCertificateData(
       val recordId : Int,
       val pictureUrl : String,
       var likes : Int,
       var isLiked : Boolean,
       val createdAt : String,
       var isSelected : Boolean = false
    )
}