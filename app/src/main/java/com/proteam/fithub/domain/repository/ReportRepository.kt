package com.proteam.fithub.domain.repository

import com.proteam.fithub.domain.source.ReportSource
import com.proteam.fithub.presentation.util.BaseResponse
import javax.inject.Inject

interface ReportRepository {

    suspend fun requestReportUser(userId : Int) : Result<BaseResponse>

    suspend fun requestReportArticle(articleId : Int) : Result<BaseResponse>

    suspend fun requestReportRecord(recordId : Int) : Result<BaseResponse>

    suspend fun requestReportComment(commentId : Int) : Result<BaseResponse>
}