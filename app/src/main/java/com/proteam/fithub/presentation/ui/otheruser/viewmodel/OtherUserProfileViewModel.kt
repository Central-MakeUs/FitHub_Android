package com.proteam.fithub.presentation.ui.otheruser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.data.remote.response.ResponseOtherUserProfileData
import com.proteam.fithub.domain.repository.ArticleRepository
import com.proteam.fithub.domain.repository.ExerciseRepository
import com.proteam.fithub.domain.repository.MyPageRepository
import com.proteam.fithub.domain.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class OtherUserProfileViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    private val articleRepository: ArticleRepository,
    private val exerciseRepository: ExerciseRepository,
    private val myPageRepository: MyPageRepository): ViewModel() {

    private val _otherUserProfile = MutableLiveData<ResponseOtherUserProfileData.ResultOtherUserProfileData>()
    val otherUserProfile : LiveData<ResponseOtherUserProfileData.ResultOtherUserProfileData> = _otherUserProfile

    private val _exerciseFilters = MutableLiveData<MutableList<ResponseExercises.ExercisesList>>()
    val exerciseFilters: LiveData<MutableList<ResponseExercises.ExercisesList>> = _exerciseFilters

    private val _selectedFilter = MutableLiveData<Int>()
    val selectedFilter: LiveData<Int> = _selectedFilter

    private val _reportStatus = MutableLiveData<Int>()
    val reportStatus : LiveData<Int> = _reportStatus

    init {
        requestExerciseFilterList()
    }

    fun setSelectedFilter(index: Int) {
        _selectedFilter.value = index
    }

    private fun requestExerciseFilterList() {
        viewModelScope.launch {
            exerciseRepository.requestExercises()
                .onSuccess { _exerciseFilters.value = it as MutableList }
        }
    }

    fun requestOtherUserProfile(userId : Int) {
        viewModelScope.launch {
            myPageRepository.requestOtherUserProfile(userId)
                .onSuccess { _otherUserProfile.value = it }
        }
    }

    fun requestOtherUserArticles(userId : Int, filter : Int) : Flow<PagingData<ResponseArticleData.ResultArticleData>> {
        return articleRepository.requestOtherUserArticleData(userId, filter)
    }

    fun requestReportUser(userId: Int) {
        viewModelScope.launch {
            reportRepository.requestReportUser(userId)
                .onSuccess { _reportStatus.value = it.code }
                .onFailure { _reportStatus.value = it.message?.toInt() }
        }
    }
}