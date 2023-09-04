package com.proteam.fithub.presentation.ui.managewrite.article.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.proteam.fithub.data.remote.request.RequestDeleteMyArticles
import com.proteam.fithub.data.remote.response.ResponseMyArticleData
import com.proteam.fithub.data.remote.response.ResponseMyCertificateData
import com.proteam.fithub.domain.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageMyArticleViewModel @Inject constructor(private val articleRepository: ArticleRepository): ViewModel() {
    private val _selectItems = MutableLiveData<MutableList<Int>>().apply { value = mutableListOf() }
    val selectItems: LiveData<MutableList<Int>> = _selectItems

    private val _deleteStatus = MutableLiveData<Int>()
    val deleteStatus: LiveData<Int> = _deleteStatus

    var isAllClicked = MutableLiveData<Boolean>(false)

    fun initAllClicked() {
        isAllClicked.value = false
    }

    fun requestMyArticleData(filter: Int): Flow<PagingData<ResponseMyArticleData.ResultMyArticleData>> {
        return articleRepository.requestMyArticleData(filter)
    }

    fun manageSelectItems(index: Int, selected: Boolean) {
        if (selected) _selectItems.value?.add(index)
        else _selectItems.value?.remove(index)
    }

    fun manageAllSelectedItems(indexes: List<Int>) {
        if (isAllClicked.value!!) _selectItems.value = indexes.toMutableList()
        else _selectItems.value = mutableListOf()
    }

    fun requestDeleteMyArticles() {
        viewModelScope.launch {
            selectItems.value?.let {
                articleRepository.requestDeleteMyArticleData(RequestDeleteMyArticles(it))
                    .onSuccess { _deleteStatus.value = it.code }
            }
        }
    }
}