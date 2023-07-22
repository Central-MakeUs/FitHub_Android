package com.proteam.fithub.data.remote.source

import android.util.Log
import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.data.remote.response.ResponseArticleDetailData
import com.proteam.fithub.data.remote.response.ResponseArticleHeartClicked
import com.proteam.fithub.data.remote.response.ResponseArticleScrapClicked
import com.proteam.fithub.data.remote.service.ArticleService
import com.proteam.fithub.domain.source.ArticleSource
import com.proteam.fithub.presentation.util.ErrorConverter.convertAndGetCode
import com.proteam.fithub.presentation.util.ErrorConverter.setValidate
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
            in 200..399 -> Result.success(res.body()!!)
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
}