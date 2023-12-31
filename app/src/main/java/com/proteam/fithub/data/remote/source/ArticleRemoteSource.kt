package com.proteam.fithub.data.remote.source

import android.util.Log
import com.proteam.fithub.data.remote.request.RequestDeleteMyArticles
import com.proteam.fithub.data.remote.request.RequestDeleteMyCertificate
import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.data.remote.response.ResponseArticleDetailData
import com.proteam.fithub.data.remote.response.ResponseArticleHeartClicked
import com.proteam.fithub.data.remote.response.ResponseArticleScrapClicked
import com.proteam.fithub.data.remote.response.ResponsePostArticleData
import com.proteam.fithub.data.remote.service.ArticleService
import com.proteam.fithub.domain.source.ArticleSource
import com.proteam.fithub.presentation.util.BaseResponse
import com.proteam.fithub.presentation.util.ConvertToRequestBody.listConverter
import com.proteam.fithub.presentation.util.ConvertToRequestBody.textConverter
import com.proteam.fithub.presentation.util.ErrorConverter.convertAndGetCode
import com.proteam.fithub.presentation.util.ErrorConverter.setValidate
import okhttp3.MultipartBody
import javax.inject.Inject

class ArticleRemoteSource @Inject constructor(private val service : ArticleService): ArticleSource{
    override suspend fun requestArticleDetailData(articleId: Int): Result<ResponseArticleDetailData.ResultArticleDetailData> {
        val res = service.requestArticleDetail(articleId)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!.result)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestArticleHeartClicked(articleId: Int): Result<ResponseArticleHeartClicked.ResultArticleHeartClicked> {
        val res = service.requestArticleHeartClicked(articleId)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!.result)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestArticleScrapClicked(articleId: Int): Result<ResponseArticleScrapClicked.ResultArticleScrapClicked> {
        val res = service.requestArticleScrapClicked(articleId)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestPostArticleData(
        categoryId: Int,
        title: String,
        contents: String,
        exerciseTag: String,
        tagList: List<String>?,
        pictureList: MutableList<MultipartBody.Part>?
    ): Result<ResponsePostArticleData> {
        val res = service.requestPostArticle(categoryId, title.textConverter(), contents.textConverter(), exerciseTag.textConverter(), tagList.listConverter(), pictureList)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestModifyArticleData(
        articleId: Int,
        title: String,
        contents: String,
        category : Int,
        exerciseTag: String,
        hashTagList: List<String>?,
        newPictureList: MutableList<MultipartBody.Part>?,
        remainPictureUrlList: List<String>?
    ): Result<ResponsePostArticleData> {
        val res = service.requestModifyArticle(articleId, title.textConverter(), contents.textConverter(), category, exerciseTag.textConverter(), hashTagList.listConverter(), newPictureList, remainPictureUrlList.listConverter())
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestDeleteBoardData(articleId: Int): Result<BaseResponse> {
        val res = service.deleteArticleData(articleId)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestDeleteMyArticleData(body: RequestDeleteMyArticles): Result<BaseResponse> {
        val res = service.requestDeleteMyArticles(body)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }
}