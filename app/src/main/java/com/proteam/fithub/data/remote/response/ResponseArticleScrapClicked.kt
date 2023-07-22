package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseArticleScrapClicked(
    val result : ResultArticleScrapClicked
) : BaseResponse() {
    data class ResultArticleScrapClicked(
        val articleId : Int,
        val articleSaves : Int
    )
}
