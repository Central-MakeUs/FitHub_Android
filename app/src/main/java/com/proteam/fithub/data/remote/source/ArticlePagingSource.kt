package com.proteam.fithub.data.remote.source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.data.remote.response.ResponseCertificateData
import com.proteam.fithub.data.remote.service.ArticleService
import com.proteam.fithub.data.remote.service.CertificateService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ArticlePagingSource(
    private val type : String,
    private val ioDispatcher: CoroutineDispatcher,
    private val service : ArticleService,
    private val category : Int): PagingSource<Int, ResponseArticleData.ResultArticleData>() {
    override fun getRefreshKey(state: PagingState<Int, ResponseArticleData.ResultArticleData>): Int? {
        return state.anchorPosition?.let { it ->
            state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResponseArticleData.ResultArticleData> {
        return try {
            val page = params.key ?: 0

            val response = withContext(ioDispatcher) {
                if(type == "date") service.requestArticleData(categoryId = category, last = page)
                else service.requestArticleDataByLike(categoryId = category, last = page)
            }

            val responseCertificates = response.result.articleList

            val prevKey = if(page == 0) null else page - 1
            val nextKey = if(responseCertificates.isEmpty()) null else page + 1

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