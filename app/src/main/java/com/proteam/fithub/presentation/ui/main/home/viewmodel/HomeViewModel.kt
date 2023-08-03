package com.proteam.fithub.presentation.ui.main.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.data.remote.response.ResponseHomeData
import com.proteam.fithub.domain.repository.ExerciseRepository
import com.proteam.fithub.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val exerciseRepository: ExerciseRepository
    ): ViewModel() {
    private val _homeData = MutableLiveData<ResponseHomeData>()
    val homeData : LiveData<ResponseHomeData> = _homeData

    private val _exercises = MutableLiveData<MutableList<ResponseExercises.ExercisesList>>()
    val exercises : LiveData<MutableList<ResponseExercises.ExercisesList>> = _exercises


    fun requestExerciseCategory() {
        viewModelScope.launch {
            exerciseRepository.requestExercises()
                .onSuccess { _exercises.value = it as MutableList }
        }
    }

    fun requestHomeData() {
        viewModelScope.launch {
            homeRepository.requestHomeData()
                .onSuccess {
                    Log.e("----", "requestHomeData: $it", )
                    _homeData.value = it }
        }
    }
}