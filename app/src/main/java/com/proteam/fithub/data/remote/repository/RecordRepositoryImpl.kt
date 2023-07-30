package com.proteam.fithub.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.proteam.fithub.data.remote.response.ResponseCertificateData
import com.proteam.fithub.data.remote.service.RecordService
import com.proteam.fithub.data.remote.source.CertificatePagingSource
import com.proteam.fithub.data.remote.source.RecordPagingSource
import com.proteam.fithub.domain.repository.RecordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(private val recordService: RecordService): RecordRepository {
    override fun requestCertificateDataWithCategory(
        type: String,
        category: Int
    ): Flow<PagingData<ResponseCertificateData.ResultCertificateData>> {
        return Pager(PagingConfig(pageSize = 12)) {
            RecordPagingSource(type, Dispatchers.IO, recordService, category)
        }.flow
    }
}