package com.proteam.fithub.data.remote.source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.proteam.fithub.data.remote.response.ResponseCertificateData
import com.proteam.fithub.data.remote.service.CertificateService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CertificatePagingSource(
    private val type : String,
    private val ioDispatcher: CoroutineDispatcher,
    private val service : CertificateService,
    private val category : Int): PagingSource<Int, ResponseCertificateData.ResultCertificateData>() {
    override fun getRefreshKey(state: PagingState<Int, ResponseCertificateData.ResultCertificateData>): Int? {
        return state.anchorPosition?.let { it ->
            state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResponseCertificateData.ResultCertificateData> {
        return try {
            val page = params.key ?: 0

            val response = withContext(ioDispatcher) {
                if(type == "date") service.requestCertificateData(categoryId = category, last = page)
                else service.requestCertificateDataByLike(categoryId = category, last = page)
            }
            val responseCertificates = response.result.recordList

            val prevKey = if(page == 0) null else responseCertificates.first().recordId
            val nextKey = if(responseCertificates.isEmpty()) null else responseCertificates.last().recordId
            LoadResult.Page(
                data = responseCertificates,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception : Exception) {
            return LoadResult.Error(exception)
        }
    }
}