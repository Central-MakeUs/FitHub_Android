package com.proteam.fithub.presentation.ui.detail.board.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.proteam.fithub.data.remote.request.RequestPostComment
import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.data.remote.response.ResponseArticleDetailData
import com.proteam.fithub.data.remote.response.ResponseArticleHeartClicked
import com.proteam.fithub.data.remote.response.ResponseArticleScrapClicked
import com.proteam.fithub.data.remote.response.ResponseCommentData
import com.proteam.fithub.domain.repository.ArticleRepository
import com.proteam.fithub.domain.repository.CommentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardDetailViewModel @Inject constructor(private val repository: ArticleRepository,
    private val commentRepository: CommentRepository): ViewModel() {
    private val _articleData = MutableLiveData<ResponseArticleDetailData.ResultArticleDetailData>()
    val articleData : LiveData<ResponseArticleDetailData.ResultArticleDetailData> = _articleData

    var userInputComment = MutableLiveData<String>().apply { value = "" }
    private val _postCommentState = MutableLiveData<Int>()
    val postCommentState : LiveData<Int> = _postCommentState

    private val _heartResult =
        MutableLiveData<ResponseArticleHeartClicked.ResultArticleHeartClicked>()
    val heartResult: LiveData<ResponseArticleHeartClicked.ResultArticleHeartClicked> =
        _heartResult

    private val _scrapResult = MutableLiveData<ResponseArticleScrapClicked.ResultArticleScrapClicked>()
    val scrapResult : LiveData<ResponseArticleScrapClicked.ResultArticleScrapClicked> = _scrapResult


    fun requestData(index : Int) {
        viewModelScope.launch {
            repository.requestArticleDetailData(index)
                .onSuccess { _articleData.value = it }
        }
    }

    fun requestComment(): Flow<PagingData<ResponseCommentData.ResultCommentItems>> {
        return commentRepository.requestComments("articles", _articleData.value!!.articleId)
    }

    fun requestPostComment() {
        viewModelScope.launch {
            commentRepository.postComment("articles", _articleData.value!!.articleId, RequestPostComment(userInputComment.value!!))
                .onSuccess { _postCommentState.value = it.code }
        }
    }

    fun requestHeartClicked(articleId: Int) {
        viewModelScope.launch {
            repository.requestArticleHeartClicked(articleId)
                .onSuccess { _heartResult.value = it }
        }
    }

    fun setEffectHeart() {
        _articleData.value = _articleData.value?.apply {
            if (isLiked) likes -= 1 else likes += 1
            isLiked = isLiked.not()
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


}