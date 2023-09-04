package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseKeywordsData (
    val result : ResultKeywordsData
) : BaseResponse() {
    data class ResultKeywordsData(
        val keywordList : List<String>
    )
}