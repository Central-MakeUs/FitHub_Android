package com.proteam.fithub.domain.source

import com.proteam.fithub.data.remote.request.RequestDeleteMyCertificate
import com.proteam.fithub.data.remote.response.ResponseCertificateDetailData
import com.proteam.fithub.data.remote.response.ResponseCertificateHeartClicked
import com.proteam.fithub.data.remote.response.ResponseIsWriteTodayData
import com.proteam.fithub.data.remote.response.ResponsePostCertificateData
import com.proteam.fithub.presentation.util.BaseResponse
import okhttp3.MultipartBody

interface CertificateSource {

    suspend fun requestCertificateDetail(recordId : Int) : Result<ResponseCertificateDetailData>

    suspend fun requestCertificateHeartClicked(recordId : Int) : Result<ResponseCertificateHeartClicked>

    suspend fun requestPostCertificateData(categoryId : Int, contents : String, exerciseTag : String, hashTagList : List<String>?, image : MultipartBody.Part) : Result<ResponsePostCertificateData>

    suspend fun requestDeleteCertificateData(recordId: Int) : Result<BaseResponse>

    suspend fun requestModifyCertificateData(recordId: Int, category : Int, contents : String, exerciseTag : String, hashTagList : List<String>?, newImage : MultipartBody.Part?, remainImageUrl : String?) : Result<ResponsePostCertificateData>

    suspend fun requestDeleteMyCertificateData(body : RequestDeleteMyCertificate) : Result<BaseResponse>

    suspend fun requestCertificateToday() : Result<ResponseIsWriteTodayData.ResultIsWriteTodayData>
}
