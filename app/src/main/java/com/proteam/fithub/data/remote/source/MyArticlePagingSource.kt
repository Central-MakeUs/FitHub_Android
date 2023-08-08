package com.proteam.fithub.data.remote.source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kakao.sdk.common.KakaoSdk.type
import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.data.remote.response.ResponseMyArticleData
import com.proteam.fithub.data.remote.service.ArticleService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MyArticlePagingSource(
    private val ioDispatcher: CoroutineDispatcher,
    private val service : ArticleService,
    private val category : Int): PagingSource<Int, ResponseMyArticleData.ResultMyArticleData>() {
    override fun getRefreshKey(state: PagingState<Int, ResponseMyArticleData.ResultMyArticleData>): Int? {
        return state.anchorPosition?.let { it ->
            state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResponseMyArticleData.ResultMyArticleData> {
        return try {
            val page = params.key ?: 0
            val response = withContext(ioDispatcher) {
                service.requestMyArticleData(categoryId = category, pageIndex = page)
            }
            val responseArticles = response.result.articleList

            val prevKey = if(page == 0) null else page - 1
            val nextKey = if(responseArticles.size < 12) null else page + 1
            LoadResult.Page(
                data = responseArticles,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception : Exception) {
            return LoadResult.Error(exception)
        }
    }
}