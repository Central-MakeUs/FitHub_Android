package com.proteam.fithub.data.remote.source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.proteam.fithub.data.remote.response.ResponseCertificateData
import com.proteam.fithub.data.remote.service.RecordService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RecordPagingSource(
    private val type: String,
    private val ioDispatcher: CoroutineDispatcher,
    private val service: RecordService,
    private val category: Int
) : PagingSource<Int, ResponseCertificateData.ResultCertificateData>() {
    override fun getRefreshKey(state: PagingState<Int, ResponseCertificateData.ResultCertificateData>): Int? {
        return state.anchorPosition?.let { it ->
            state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResponseCertificateData.ResultCertificateData> {
        return try {
            val page = params.key ?: 0
            val response = withContext(ioDispatcher) {
                if (type == "date") service.requestCertificateDataWithCategory(
                    categoryId = category,
                    pageIndex = page
                )
                else service.requestCertificateDataWithCategoryByLike(
                    categoryId = category,
                    last = page
                )
            }
            val responseCertificates = response.result

            val prevKey = if(page == 0) null else page - 1
            val nextKey = if (page == responseCertificates.totalPage - 1) null else page + 1
            Log.e("----", "load: prev : $prevKey / next : $nextKey", )
            LoadResult.Page(
                data = responseCertificates.recordList,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}