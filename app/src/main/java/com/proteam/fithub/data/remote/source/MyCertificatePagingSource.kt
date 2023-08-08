package com.proteam.fithub.data.remote.source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kakao.sdk.common.KakaoSdk.type
import com.proteam.fithub.data.remote.response.ResponseCertificateData
import com.proteam.fithub.data.remote.response.ResponseMyCertificateData
import com.proteam.fithub.data.remote.service.CertificateService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MyCertificatePagingSource(
    private val ioDispatcher: CoroutineDispatcher,
    private val service : CertificateService,
    private val category : Int): PagingSource<Int, ResponseMyCertificateData.ResultMyCertificateData>() {
    override fun getRefreshKey(state: PagingState<Int, ResponseMyCertificateData.ResultMyCertificateData>): Int? {
        return state.anchorPosition?.let { it ->
            state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResponseMyCertificateData.ResultMyCertificateData> {
        return try {
            val page = params.key ?: 0
            val response = withContext(ioDispatcher) {
                service.requestMyCertificateData(categoryId = category, pageIndex = page)
            }
            val responseCertificates = response.result.recordList

            val prevKey = if(page == 0) null else page - 1
            val nextKey = if(responseCertificates.size < 12) null else page + 1
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