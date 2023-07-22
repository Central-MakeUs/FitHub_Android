package com.proteam.fithub.domain.repository

import androidx.paging.PagingData
import com.proteam.fithub.data.remote.response.ResponseCertificateData
import com.proteam.fithub.data.remote.response.ResponseCertificateDetailData
import com.proteam.fithub.data.remote.response.ResponseCertificateHeartClicked
import com.proteam.fithub.data.remote.response.ResponsePostCertificateData
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface CertificateRepository {

    fun requestCertificateData(type : String, category : Int) : Flow<PagingData<ResponseCertificateData.ResultCertificateData>>

    suspend fun requestCertificateDetail(recordId : Int) : Result<ResponseCertificateDetailData.ResultCertificateDetailData>

    suspend fun requestCertificateHeartClicked(recordId : Int) : Result<ResponseCertificateHeartClicked.ResultCertificateHeartClicked>

    suspend fun requestPostCertificateData(categoryId : Int, contents : String, exerciseTag : String, hashTagList : List<String>, image : MultipartBody.Part) : Result<ResponsePostCertificateData>
}