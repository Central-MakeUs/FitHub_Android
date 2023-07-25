package com.proteam.fithub.data.remote.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.proteam.fithub.data.remote.response.ResponseCommentData
import com.proteam.fithub.data.remote.service.CommentService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CommentPagingSource(
    private val type : String,
    private val id : Int,
    private val ioDispatcher: CoroutineDispatcher,
    private val service : CommentService): PagingSource<Int, ResponseCommentData.ResultCommentItems>() {
    override fun getRefreshKey(state: PagingState<Int, ResponseCommentData.ResultCommentItems>): Int? {
        return state.anchorPosition?.let { it ->
            state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResponseCommentData.ResultCommentItems> {
        return try {
            val page = params.key ?: 0

            val response = withContext(ioDispatcher) {
                service.requestCommentData(type = type, id = id, last = page)
            }

            val responseComments = response.result.commentList

            val prevKey = if(page == 0) null else responseComments.first().commentId
            val nextKey = if(responseComments.isEmpty()) null else responseComments.last().commentId
            LoadResult.Page(
                data = responseComments,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception : Exception) {
            return LoadResult.Error(exception)
        }
    }
}