package com.proteam.fithub.domain.source

import com.proteam.fithub.data.remote.response.ResponseCertificateDetailData
import com.proteam.fithub.data.remote.response.ResponseCertificateHeartClicked
import com.proteam.fithub.data.remote.response.ResponsePostCertificateData
import com.proteam.fithub.presentation.util.BaseResponse
import okhttp3.MultipartBody

interface CertificateSource {

    suspend fun requestCertificateDetail(recordId : Int) : Result<ResponseCertificateDetailData.ResultCertificateDetailData>

    suspend fun requestCertificateHeartClicked(recordId : Int) : Result<ResponseCertificateHeartClicked.ResultCertificateHeartClicked>

    suspend fun requestPostCertificateData(categoryId : Int, contents : String, exerciseTag : String, hashTagList : List<String>?, image : MultipartBody.Part) : Result<ResponsePostCertificateData>

    suspend fun requestDeleteCertificateData(recordId: Int) : Result<BaseResponse>
}
