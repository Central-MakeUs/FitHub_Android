package com.proteam.fithub.domain.repository

import androidx.paging.PagingData
import com.proteam.fithub.data.remote.response.ResponseArticleData
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {

    fun requestBookmarkData(categoryId : Int) : Flow<PagingData<ResponseArticleData.ResultArticleData>>
}