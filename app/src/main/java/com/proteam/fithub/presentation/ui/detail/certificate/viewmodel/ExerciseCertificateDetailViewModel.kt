package com.proteam.fithub.presentation.ui.detail.certificate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.data.remote.response.ResponseCertificateDetailData
import com.proteam.fithub.data.remote.response.ResponseCertificateHeartClicked
import com.proteam.fithub.domain.repository.CertificateRepository
import com.proteam.fithub.domain.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseCertificateDetailViewModel @Inject constructor(
    private val certificateRepository: CertificateRepository,
    private val reportRepository: ReportRepository
) : ViewModel() {
    /** Status **/
    private val _certificateStatus = MutableLiveData<Int>()
    val certificateStatus : LiveData<Int> = _certificateStatus

    private val _deleteCertificateStatus = MutableLiveData<Int>()
    val deleteCertificateStatus : LiveData<Int> = _deleteCertificateStatus

    private val _heartStatus = MutableLiveData<Int>()
    val heartStatus : LiveData<Int> = _heartStatus

    private val _reportUserStatus = MutableLiveData<Int>()
    val reportUserStatus : LiveData<Int> = _reportUserStatus
    private val _reportRecordStatus = MutableLiveData<Int>()
    val reportRecordStatus : LiveData<Int> = _reportRecordStatus
    private val _reportCommentStatus = MutableLiveData<Int>()
    val reportCommentStatus : LiveData<Int> = _reportCommentStatus

    private val _certificateData =
        MutableLiveData<ResponseCertificateDetailData.ResultCertificateDetailData>()
    val certificateData: LiveData<ResponseCertificateDetailData.ResultCertificateDetailData> =
        _certificateData



    private val _heartResult =
        MutableLiveData<ResponseCertificateHeartClicked.ResultCertificateHeartClicked>()
    val heartResult: LiveData<ResponseCertificateHeartClicked.ResultCertificateHeartClicked> =
        _heartResult

    var userInputComment = MutableLiveData<String>().apply { value = "" }

    fun requestData(index: Int) {
        viewModelScope.launch {
            certificateRepository.requestCertificateDetail(index)
                .onSuccess {
                    _certificateStatus.value = it.code
                    _certificateData.value = it.result
                }
                .onFailure {
                    _certificateStatus.value = it.message?.toInt()
                }
        }
    }

    fun requestHeartClicked(recordId: Int) {
        viewModelScope.launch {
            certificateRepository.requestCertificateHeartClicked(recordId)
                .onSuccess { _heartResult.value = it.result }
        }
    }

    fun setEffectHeart() {
        _certificateData.value = _certificateData.value?.apply {
            this.likes = _heartResult.value!!.newLikes
            this.isLiked = _heartResult.value!!.isLiked
        }
    }

    fun requestDeleteCertificate() {
        viewModelScope.launch {
            certificateRepository.requestDeleteCertificateData(_certificateData.value!!.recordId)
                .onSuccess { _deleteCertificateStatus.value = it.code }
                .onFailure { _deleteCertificateStatus.value = it.message?.toInt() }
        }
    }

    fun requestReportUser(userId : Int) {
        viewModelScope.launch {
            reportRepository.requestReportUser(userId)
                .onSuccess { _reportUserStatus.value = it.code }
                .onFailure { _reportUserStatus.value = it.message?.toInt() }
        }
    }

    fun requestReportRecord(recordId : Int) {
        viewModelScope.launch {
            reportRepository.requestReportRecord(recordId)
                .onSuccess { _reportRecordStatus.value = it.code }
                .onFailure { _reportRecordStatus.value = it.message?.toInt() }
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