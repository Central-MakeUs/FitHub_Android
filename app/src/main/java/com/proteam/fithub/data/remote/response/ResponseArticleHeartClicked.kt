package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseArticleHeartClicked(
    val result : ResultArticleHeartClicked
) : BaseResponse() {
    data class ResultArticleHeartClicked(
        val articleId : Int,
        val articleLikes : Int
    )
}
