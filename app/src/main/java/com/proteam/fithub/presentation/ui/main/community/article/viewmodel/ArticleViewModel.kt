package com.proteam.fithub.presentation.ui.main.community.article.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.domain.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(private val articleRepository: ArticleRepository) :
    ViewModel() {

    private val _isArticleRecentSort = MutableLiveData<Boolean>(true)
    val isArticleRecentSort: LiveData<Boolean> = _isArticleRecentSort

    fun changeBoardRecentSort(state: Boolean) {
        _isArticleRecentSort.value = state
    }

    fun requestArticleItems(
        type: String,
        category: Int
    ): Flow<PagingData<ResponseArticleData.ResultArticleData>> {
        return articleRepository.requestArticleData(type, category)
    }
}