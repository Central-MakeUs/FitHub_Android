package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseSearchTotalData (
    val result : ResultSearchTotalData
) : BaseResponse() {
    data class ResultSearchTotalData(
        val articlePreview : ArticleList,
        val recordPreview : RecordList
    )
    data class ArticleList (
        val articleList: List<ResponseArticleData.ResultArticleData>
    )
    data class RecordList(
        val recordList : List<ResponseCertificateData.ResultCertificateData>
    )
}