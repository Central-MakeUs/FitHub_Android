package com.proteam.fithub.presentation.ui.detail.common

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.proteam.fithub.data.remote.request.RequestPostComment
import com.proteam.fithub.data.remote.response.ResponseCommentData
import com.proteam.fithub.data.remote.response.ResponseCommentHeartClicked
import com.proteam.fithub.domain.repository.CommentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(private val commentRepository: CommentRepository) :
    ViewModel() {

    private val _type = MutableLiveData<String>()
    val type: LiveData<String> = _type

    private val _index = MutableLiveData<Int>()
    val index: LiveData<Int> = _index

    private val _requestCommentListStatus = MutableLiveData<Int>()
    val requestCommentListStatus: LiveData<Int> = _requestCommentListStatus

    private val _heartStatus = MutableLiveData<Int>()
    val heartStatus : LiveData<Int> = _heartStatus

    private val _commentHeartResult =
        MutableLiveData<ResponseCommentHeartClicked>()
    val commentHeartResult: LiveData<ResponseCommentHeartClicked> =
        _commentHeartResult

    private val _postCommentState = MutableLiveData<Int>()
    val postCommentState : LiveData<Int> = _postCommentState

    fun requestCommentList(type : String, index : Int): Flow<PagingData<ResponseCommentData.ResultCommentItems>> {
        return commentRepository.requestComments(type, index)
    }

    fun requestDeleteComment(type: String, id: Int, commentId: Int) {
        viewModelScope.launch {
            commentRepository.postDeleteComment(type, id, commentId)
                .onSuccess { Log.e("----", "requestDeleteComment: SUCCESS") }
                .onFailure { Log.e("----", "requestDeleteComment: ${it.message}") }
        }
    }

    fun requestCommentHeartClicked(type : String, id : Int, index : Int) {
        viewModelScope.launch {
            commentRepository.postCommentHeartClicked(type, id, index)
                .onSuccess {
                    _heartStatus.value = it.code
                    _commentHeartResult.value = it }
                .onFailure { _heartStatus.value = it.message?.toInt() }
        }
        initHeartStatus()
    }

    private fun initHeartStatus() {
        _heartStatus.value = 0
    }


    fun requestPostComment(type : String, id : Int, content : String) {
        viewModelScope.launch {
            commentRepository.postComment(type, id, RequestPostComment(content))
                .onSuccess { _postCommentState.value = it.code }
        }
    }
}