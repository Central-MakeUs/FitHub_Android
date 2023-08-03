package com.proteam.fithub.presentation.ui.sign.up.common.exercise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.domain.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterestExerciseViewModel @Inject constructor(private val exerciseRepository: ExerciseRepository): ViewModel() {

    private val _exercises = MutableLiveData<MutableList<ResponseExercises.ExercisesList>>()
    val exercises: LiveData<MutableList<ResponseExercises.ExercisesList>> = _exercises

    private val _selectedExercises = MutableLiveData<Int>()
    val selectExercises: LiveData<Int> = _selectedExercises

    fun requestExercises() {
        viewModelScope.launch {
            exerciseRepository.requestExercises()
                .onSuccess { _exercises.value = it as MutableList}
        }
    }

    fun setInterestSports(sportsId: Int) {
        _selectedExercises.value = sportsId
    }
}