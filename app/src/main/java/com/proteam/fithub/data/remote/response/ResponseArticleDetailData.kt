package com.proteam.fithub.data.remote.response

import com.proteam.fithub.data.data.CategoryData
import com.proteam.fithub.data.data.ComponentUserData
import com.proteam.fithub.data.data.HashTagData
import com.proteam.fithub.presentation.util.BaseResponse
import java.io.Serializable

data class ResponseArticleDetailData(
    val result : ResultArticleDetailData
) : BaseResponse() {
    data class ResultArticleDetailData(
        val articleId : Int,
        val articleCategory : CategoryData,
        val userInfo : ComponentUserData,
        val title : String,
        val contents : String,
        val articlePictureList : ArticlePictureItems,
        val createdAt : String,
        var likes : Int,
        var comments : Int,
        var scraps : Int,
        var isLiked : Boolean,
        var isScraped : Boolean,
        val hashtags : HashTagData
    )

    data class ArticlePictureItems(
        val pictureList : List<ArticlePictureResult?>
    )

    data class ArticlePictureResult(
        val pictureId : Int,
        val pictureUrl : String
    )
}
