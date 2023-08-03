package com.proteam.fithub.data.remote.repository

import com.proteam.fithub.domain.repository.ReportRepository
import com.proteam.fithub.domain.source.ReportSource
import com.proteam.fithub.presentation.util.BaseResponse
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(private val source: ReportSource): ReportRepository {
    override suspend fun requestReportUser(userId: Int): Result<BaseResponse> {
        return source.requestReportUser(userId)
    }

    override suspend fun requestReportArticle(articleId: Int): Result<BaseResponse> {
        return source.requestReportArticle(articleId)
    }

    override suspend fun requestReportRecord(recordId: Int): Result<BaseResponse> {
        return source.requestReportRecord(recordId)
    }

    override suspend fun requestReportComment(commentId: Int): Result<BaseResponse> {
        return source.requestReportComment(commentId)
    }


}