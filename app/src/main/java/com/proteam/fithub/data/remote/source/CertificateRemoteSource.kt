package com.proteam.fithub.data.remote.source

import android.util.Log
import com.proteam.fithub.data.remote.response.ResponseCertificateDetailData
import com.proteam.fithub.data.remote.response.ResponseCertificateHeartClicked
import com.proteam.fithub.data.remote.response.ResponsePostCertificateData
import com.proteam.fithub.data.remote.service.CertificateService
import com.proteam.fithub.domain.source.CertificateSource
import com.proteam.fithub.presentation.util.BaseResponse
import com.proteam.fithub.presentation.util.ErrorConverter.convertAndGetCode
import com.proteam.fithub.presentation.util.ErrorConverter.setValidate
import com.proteam.fithub.presentation.util.ErrorConverter.setValidate2
import okhttp3.MultipartBody
import javax.inject.Inject

class CertificateRemoteSource @Inject constructor(private val service : CertificateService): CertificateSource {

    override suspend fun requestCertificateDetail(recordId: Int): Result<ResponseCertificateDetailData.ResultCertificateDetailData> {
        val res = service.requestCertificateDetailData(recordId)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()?.result!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestCertificateHeartClicked(recordId: Int): Result<ResponseCertificateHeartClicked.ResultCertificateHeartClicked> {
        val res = service.requestClickCertificateHeart(recordId)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()?.result!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestPostCertificateData(
        categoryId: Int,
        contents: String,
        exerciseTag: String,
        hashTagList: List<String>?,
        image: MultipartBody.Part
    ): Result<ResponsePostCertificateData> {
        val res = service.postCertificateData(categoryId, contents, exerciseTag, hashTagList, image)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestDeleteCertificateData(recordId: Int): Result<BaseResponse> {
        val res = service.deleteCertificateData(recordId)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }
}