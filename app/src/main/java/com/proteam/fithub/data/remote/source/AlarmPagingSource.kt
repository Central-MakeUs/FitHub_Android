package com.proteam.fithub.data.remote.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.proteam.fithub.data.remote.response.ResponseAlarmData
import com.proteam.fithub.data.remote.service.AlarmService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AlarmPagingSource(
    private val ioDispatcher: CoroutineDispatcher,
    private val service : AlarmService): PagingSource<Int, ResponseAlarmData.ResultAlarmData>() {
    override fun getRefreshKey(state: PagingState<Int, ResponseAlarmData.ResultAlarmData>): Int? {
        return state.anchorPosition?.let { it ->
            state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResponseAlarmData.ResultAlarmData> {
        return try {
            val page = params.key ?: 0

            val response = withContext(ioDispatcher) {
                service.requestAlarmListData(pageIndex = page)
            }

            val responseComments = response.result.alarmList

            val prevKey = if(page == 0) null else page - 1
            val nextKey = if(responseComments.isEmpty()) null else page + 1
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