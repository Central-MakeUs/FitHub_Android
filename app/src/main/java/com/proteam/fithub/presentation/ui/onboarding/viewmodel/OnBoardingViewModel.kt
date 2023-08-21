package com.proteam.fithub.presentation.ui.onboarding.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OnBoardingViewModel : ViewModel() {

    private val _onBoardingPosition = MutableLiveData<Int>(0)
    val onBoardingPosition : LiveData<Int> = _onBoardingPosition

    fun setPagerPosition(position : Int) {
        _onBoardingPosition.value = position
    }
}