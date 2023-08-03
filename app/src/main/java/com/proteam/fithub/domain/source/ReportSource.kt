package com.proteam.fithub.domain.source

import com.proteam.fithub.presentation.util.BaseResponse

interface ReportSource {

    suspend fun requestReportUser(userId : Int) : Result<BaseResponse>

    suspend fun requestReportArticle(articleId : Int) : Result<BaseResponse>

    suspend fun requestReportRecord(recordId : Int) : Result<BaseResponse>

    suspend fun requestReportComment(commentId : Int) : Result<BaseResponse>

}