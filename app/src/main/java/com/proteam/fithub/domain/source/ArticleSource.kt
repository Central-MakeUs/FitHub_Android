package com.proteam.fithub.domain.source

import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.data.remote.response.ResponseArticleDetailData
import com.proteam.fithub.data.remote.response.ResponseArticleHeartClicked
import com.proteam.fithub.data.remote.response.ResponseArticleScrapClicked
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ArticleSource {

    suspend fun requestArticleDetailData(articleId : Int) : Result<ResponseArticleDetailData.ResultArticleDetailData>

    suspend fun requestArticleHeartClicked(articleId : Int) : Result<ResponseArticleHeartClicked.ResultArticleHeartClicked>

    suspend fun requestArticleScrapClicked(articleId : Int) : Result<ResponseArticleScrapClicked.ResultArticleScrapClicked>
}