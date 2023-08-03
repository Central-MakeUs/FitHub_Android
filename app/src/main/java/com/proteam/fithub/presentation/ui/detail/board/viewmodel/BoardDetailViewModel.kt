package com.proteam.fithub.presentation.ui.detail.board.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.proteam.fithub.data.remote.request.RequestPostComment
import com.proteam.fithub.data.remote.response.ResponseArticleDetailData
import com.proteam.fithub.data.remote.response.ResponseArticleHeartClicked
import com.proteam.fithub.data.remote.response.ResponseArticleScrapClicked
import com.proteam.fithub.data.remote.response.ResponseCommentData
import com.proteam.fithub.domain.repository.ArticleRepository
import com.proteam.fithub.domain.repository.CommentRepository
import com.proteam.fithub.domain.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardDetailViewModel @Inject constructor(
    private val repository: ArticleRepository,
    private val reportRepository: ReportRepository): ViewModel() {
    private val _articleData = MutableLiveData<ResponseArticleDetailData.ResultArticleDetailData>()
    val articleData : LiveData<ResponseArticleDetailData.ResultArticleDetailData> = _articleData

    var userInputComment = MutableLiveData<String>().apply { value = "" }

    private val _heartResult =
        MutableLiveData<ResponseArticleHeartClicked.ResultArticleHeartClicked>()
    val heartResult: LiveData<ResponseArticleHeartClicked.ResultArticleHeartClicked> =
        _heartResult

    private val _reportUserStatus = MutableLiveData<Int>()
    val reportUserStatus : LiveData<Int> = _reportUserStatus
    private val _reportArticleStatus = MutableLiveData<Int>()
    val reportArticleStatus : LiveData<Int> = _reportArticleStatus
    private val _reportCommentStatus = MutableLiveData<Int>()
    val reportCommentStatus : LiveData<Int> = _reportCommentStatus

    private val _scrapResult = MutableLiveData<ResponseArticleScrapClicked.ResultArticleScrapClicked>()
    val scrapResult : LiveData<ResponseArticleScrapClicked.ResultArticleScrapClicked> = _scrapResult


    fun requestData(index : Int) {
        viewModelScope.launch {
            repository.requestArticleDetailData(index)
                .onSuccess { _articleData.value = it }
        }
    }

    fun requestHeartClicked(articleId: Int) {
        viewModelScope.launch {
            repository.requestArticleHeartClicked(articleId)
                .onSuccess { _heartResult.value = it }
        }
    }

    fun setEffectHeart(heartResult : ResponseArticleHeartClicked.ResultArticleHeartClicked) {
        _articleData.value = _articleData.value?.apply {
            this.isLiked = heartResult.isLiked
            this.likes = heartResult.articleLikes
        }
    }

    fun requestScrapClicked(articleId: Int) {
        viewModelScope.launch {
            repository.requestArticleScrapClicked(articleId)
                .onSuccess { _scrapResult.value = it }
        }
    }

    fun setEffectScrap() {
        _articleData.value = _articleData.value?.apply {
            isScraped = isScraped.not()
        }
    }

    fun requestDeleteArticle() {
        viewModelScope.launch {
            repository.requestDeleteArticleData(_articleData.value!!.articleId)
        }
    }

    fun requestReportUser(userId : Int) {
        viewModelScope.launch {
            reportRepository.requestReportUser(userId)
                .onSuccess { _reportUserStatus.value = it.code }
                .onFailure { _reportUserStatus.value = it.message?.toInt() }
        }
    }

    fun requestReportArticle(articleId : Int) {
        viewModelScope.launch {
            reportRepository.requestReportArticle(articleId)
                .onSuccess { _reportArticleStatus.value = it.code }
                .onFailure { _reportArticleStatus.value = it.message?.toInt() }
        }
    }

    fun requestReportComment(commentId : Int) {
        viewModelScope.launch {
            reportRepository.requestReportComment(commentId)
                .onSuccess { _reportCommentStatus.value = it.code }
                .onFailure { _reportCommentStatus.value = it.message?.toInt() }
        }
    }

}