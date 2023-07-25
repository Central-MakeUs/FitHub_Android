package com.proteam.fithub.presentation.ui.sign.up.common.profile.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.R
import com.proteam.fithub.domain.repository.SignUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val signUpRepository: SignUpRepository): ViewModel() {
    val profileUploaded = MutableLiveData<Boolean>(false)

    private val _userSelectedProfileImage = MutableLiveData<Any>().apply { value = R.drawable.ic_sign_up_default_profile }
    val userSelectedProfileImage: LiveData<Any> = _userSelectedProfileImage

    private val _checkNickNameResult = MutableLiveData<Int>()
    val checkNickNameResult: LiveData<Int> = _checkNickNameResult


    fun requestCheckSameNickName(nickname: String) {
        viewModelScope.launch {
            signUpRepository.requestCheckSameNickName(nickname)
                .onSuccess { _checkNickNameResult.value = it.code }
        }
    }

    fun setUserSelectedProfile(path: Uri) {
        profileUploaded.value = true
        _userSelectedProfileImage.value = path
    }
}