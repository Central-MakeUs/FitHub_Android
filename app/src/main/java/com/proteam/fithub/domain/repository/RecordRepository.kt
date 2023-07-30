package com.proteam.fithub.domain.repository

import androidx.paging.PagingData
import com.proteam.fithub.data.remote.response.ResponseCertificateData
import kotlinx.coroutines.flow.Flow

interface RecordRepository {

    fun requestCertificateDataWithCategory(type : String, category : Int) : Flow<PagingData<ResponseCertificateData.ResultCertificateData>>
}