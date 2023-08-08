package com.proteam.fithub.data.remote.response

import com.proteam.fithub.data.data.CategoryData
import com.proteam.fithub.data.data.ComponentUserData
import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseMyArticleData (
    val result : ListResultMyArticleData
) : BaseResponse() {
    data class ListResultMyArticleData(
        val articleList : List<ResultMyArticleData>
    )
    data class ResultMyArticleData(
        val articleId : Int,
        val userInfo : ComponentUserData,
        val articleCategory : CategoryData,
        val title : String,
        val contents : String,
        val pictureUrl : String?,
        var exerciseTag : String,
        var likes : Int,
        var isLiked : Boolean,
        val comments : Int,
        val createdAt : String,
        var isSelected : Boolean = false
    )
}