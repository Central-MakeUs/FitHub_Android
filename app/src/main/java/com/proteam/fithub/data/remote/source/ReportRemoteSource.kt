package com.proteam.fithub.data.remote.source

import com.proteam.fithub.data.remote.service.ReportService
import com.proteam.fithub.domain.source.ReportSource
import com.proteam.fithub.presentation.util.BaseResponse
import com.proteam.fithub.presentation.util.ErrorConverter.convertAndGetCode
import javax.inject.Inject

class ReportRemoteSource @Inject constructor(private val service : ReportService): ReportSource {

    override suspend fun requestReportUser(userId: Int): Result<BaseResponse> {
        val res = service.postUserReport(userId)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestReportArticle(articleId: Int): Result<BaseResponse> {
        val res = service.postArticleReport(articleId)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestReportRecord(recordId: Int): Result<BaseResponse> {
        val res = service.postRecordReport(recordId)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestReportComment(commentId: Int): Result<BaseResponse> {
        val res = service.postCommentReport(commentId)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }
}