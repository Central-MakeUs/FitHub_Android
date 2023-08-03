package com.proteam.fithub.data.remote.source

import android.util.Log
import com.proteam.fithub.data.remote.response.ResponseCertificateDetailData
import com.proteam.fithub.data.remote.response.ResponseCertificateHeartClicked
import com.proteam.fithub.data.remote.response.ResponsePostCertificateData
import com.proteam.fithub.data.remote.service.CertificateService
import com.proteam.fithub.domain.source.CertificateSource
import com.proteam.fithub.presentation.util.BaseResponse
import com.proteam.fithub.presentation.util.ConvertToRequestBody.listConverter
import com.proteam.fithub.presentation.util.ConvertToRequestBody.textConverter
import com.proteam.fithub.presentation.util.ErrorConverter.convertAndGetCode
import okhttp3.MultipartBody
import javax.inject.Inject

class CertificateRemoteSource @Inject constructor(private val service : CertificateService): CertificateSource {

    override suspend fun requestCertificateDetail(recordId: Int): Result<ResponseCertificateDetailData> {
        val res = service.requestCertificateDetailData(recordId)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestCertificateHeartClicked(recordId: Int): Result<ResponseCertificateHeartClicked> {
        val res = service.requestClickCertificateHeart(recordId)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
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
        val res = service.postCertificateData(categoryId, contents.textConverter(), exerciseTag.textConverter(), hashTagList?.listConverter(), image)
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

    override suspend fun requestModifyCertificateData(
        recordId: Int,
        category: Int,
        contents: String,
        exerciseTag: String,
        hashTagList: List<String>?,
        newImage: MultipartBody.Part?,
        remainImageUrl: String?
    ): Result<ResponsePostCertificateData> {
        Log.e("----", "requestModifyCertificateData: ${remainImageUrl}", )
        val res = service.modifyCertificateData(recordId, category, contents.textConverter(), exerciseTag.textConverter(), hashTagList?.listConverter(), newImage, remainImageUrl?.textConverter())
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

}