package com.proteam.fithub.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kakao.sdk.common.KakaoSdk.type
import com.proteam.fithub.data.remote.request.RequestDeleteMyCertificate
import com.proteam.fithub.data.remote.response.ResponseCertificateData
import com.proteam.fithub.data.remote.response.ResponseCertificateDetailData
import com.proteam.fithub.data.remote.response.ResponseCertificateHeartClicked
import com.proteam.fithub.data.remote.response.ResponseIsWriteTodayData
import com.proteam.fithub.data.remote.response.ResponseMyCertificateData
import com.proteam.fithub.data.remote.response.ResponsePostCertificateData
import com.proteam.fithub.data.remote.service.CertificateService
import com.proteam.fithub.data.remote.source.CertificatePagingSource
import com.proteam.fithub.data.remote.source.MyCertificatePagingSource
import com.proteam.fithub.domain.repository.CertificateRepository
import com.proteam.fithub.domain.source.CertificateSource
import com.proteam.fithub.presentation.util.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class CertificateRepositoryImpl @Inject constructor(
    private val service: CertificateService,
    private val source : CertificateSource
) : CertificateRepository {
    override fun requestCertificateData(type : String, category: Int): Flow<PagingData<ResponseCertificateData.ResultCertificateData>> {
        return Pager(PagingConfig(pageSize = 12)) {
            CertificatePagingSource(type, Dispatchers.IO, service, category)
        }.flow
    }

    override suspend fun requestCertificateDetail(recordId: Int): Result<ResponseCertificateDetailData> {
        return source.requestCertificateDetail(recordId)
    }

    override suspend fun requestCertificateHeartClicked(recordId: Int): Result<ResponseCertificateHeartClicked> {
        return source.requestCertificateHeartClicked(recordId)
    }

    override suspend fun requestPostCertificateData(
        categoryId: Int,
        contents: String,
        exerciseTag: String,
        hashTagList: List<String>?,
        image: MultipartBody.Part
    ): Result<ResponsePostCertificateData> {
        return source.requestPostCertificateData(categoryId, contents, exerciseTag, hashTagList, image)
    }

    override suspend fun requestDeleteCertificateData(recordId: Int): Result<BaseResponse> {
        return source.requestDeleteCertificateData(recordId)
    }

    override suspend fun requestModifyCertificateData(
        recordId: Int,
        category: Int,
        contents: String,
        exerciseTag: String,
        hashTagList: List<String>?,
        newImage: MultipartBody.Part?,
        remainImageUrl: String?
    ): Result<ResponsePostCertificateData> {
        return source.requestModifyCertificateData(recordId, category, contents, exerciseTag, hashTagList, newImage, remainImageUrl)
    }

    override fun requestMyCertificateData(category: Int): Flow<PagingData<ResponseMyCertificateData.ResultMyCertificateData>> {
        return Pager(PagingConfig(pageSize = 12)) {
            MyCertificatePagingSource(Dispatchers.IO, service, category)
        }.flow
    }

    override suspend fun requestDeleteMyCertificateData(body: RequestDeleteMyCertificate): Result<BaseResponse> {
        return source.requestDeleteMyCertificateData(body)
    }

    override suspend fun requestCertificateToday(): Result<ResponseIsWriteTodayData.ResultIsWriteTodayData> {
        return source.requestCertificateToday()
    }


}