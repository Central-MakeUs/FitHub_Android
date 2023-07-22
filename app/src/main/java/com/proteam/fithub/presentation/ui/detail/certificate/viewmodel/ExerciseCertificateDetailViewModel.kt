package com.proteam.fithub.presentation.ui.detail.certificate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.proteam.fithub.data.remote.request.RequestPostComment
import com.proteam.fithub.data.remote.response.ResponseCertificateDetailData
import com.proteam.fithub.data.remote.response.ResponseCertificateHeartClicked
import com.proteam.fithub.data.remote.response.ResponseCommentData
import com.proteam.fithub.domain.repository.CertificateRepository
import com.proteam.fithub.domain.repository.CommentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseCertificateDetailViewModel @Inject constructor(
    private val certificateRepository: CertificateRepository,
    private val commentRepository: CommentRepository
) : ViewModel() {
    private val _certificateData =
        MutableLiveData<ResponseCertificateDetailData.ResultCertificateDetailData>()
    val certificateData: LiveData<ResponseCertificateDetailData.ResultCertificateDetailData> =
        _certificateData

    private val _heartResult =
        MutableLiveData<ResponseCertificateHeartClicked.ResultCertificateHeartClicked>()
    val heartResult: LiveData<ResponseCertificateHeartClicked.ResultCertificateHeartClicked> =
        _heartResult

    var userInputComment = MutableLiveData<String>().apply { value = "" }
    private val _postCommentState = MutableLiveData<Int>()
    val postCommentState : LiveData<Int> = _postCommentState

    fun requestData(index: Int) {
        viewModelScope.launch {
            certificateRepository.requestCertificateDetail(index)
                .onSuccess { _certificateData.value = it }
        }
    }

    fun requestComment(): Flow<PagingData<ResponseCommentData.ResultCommentItems>> {
        return commentRepository.requestComments("records", _certificateData.value!!.recordId)
    }

    fun requestPostComment() {
        viewModelScope.launch {
            commentRepository.postComment("records", _certificateData.value!!.recordId, RequestPostComment(userInputComment.value!!))
                .onSuccess { _postCommentState.value = it.code }
        }
    }

    fun requestHeartClicked(recordId: Int) {
        viewModelScope.launch {
            certificateRepository.requestCertificateHeartClicked(recordId)
                .onSuccess { _heartResult.value = it }
        }
    }

    fun setEffectHeart() {
        _certificateData.value = _certificateData.value?.apply {
            if (isLiked) likes -= 1 else likes += 1
            isLiked = isLiked.not()
        }
    }
}