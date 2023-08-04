package com.proteam.fithub.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.data.remote.service.BookmarkService
import com.proteam.fithub.data.remote.source.BookmarkPagingSource
import com.proteam.fithub.domain.repository.BookmarkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(private val service : BookmarkService): BookmarkRepository {
    override fun requestBookmarkData(categoryId: Int): Flow<PagingData<ResponseArticleData.ResultArticleData>> {
        return Pager(PagingConfig(pageSize = 12)) {
            BookmarkPagingSource(Dispatchers.IO, service, categoryId)
        }.flow
    }
}