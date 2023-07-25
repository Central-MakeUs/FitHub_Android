package com.proteam.fithub.presentation.ui.sign.up.social.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.data.remote.response.ResponseSignUp
import com.proteam.fithub.domain.repository.SignInRepository
import com.proteam.fithub.domain.repository.SignUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SocialSignUpViewModel @Inject constructor(
    private val signUpRepository: SignUpRepository,
    private val signInRepository: SignInRepository
) :
    ViewModel() {
    val TAG = "----"

    private val _userAgreement = MutableLiveData<Boolean>()
    private val _userInputName = MutableLiveData<String>()
    private val _userInputBirth = MutableLiveData<String>()
    private val _userInputGender = MutableLiveData<String>()
    private val _userInputNickName = MutableLiveData<String>()
    private val _userInputImage = MutableLiveData<String>()
    private val _userInterestExercise = MutableLiveData<List<Int>>()

    val userInputImage: LiveData<String> = _userInputImage

    private val _imagePaths = MutableLiveData<Uri>()
    val imagePaths: LiveData<Uri> = _imagePaths

    private val _signUpState = MutableLiveData<Int>()
    val signUpState: LiveData<Int> = _signUpState

    /** SET DATA **/
    fun setUserAgreements(agree: Boolean) {
        _userAgreement.value = agree
        Log.e(TAG, "setUserAgreements: ${_userAgreement.value}")
    }

    fun setUserName(name: String) {
        _userInputName.value = name
        Log.e(TAG, "setUserName: ${_userInputName.value}")
    }

    fun setUserBirth(birth: String) {
        _userInputBirth.value = birth
        Log.e(TAG, "setUserBirth: ${_userInputBirth.value}")
    }

    fun setUserGender(gender: String) {
        _userInputGender.value = gender
        Log.e(TAG, "setUserGender: ${_userInputGender.value}")
    }

    fun setUserNickName(nickname: String) {
        _userInputNickName.value = nickname
        Log.e(TAG, "setUserNickName: ${_userInputNickName.value}")
    }

    fun setUserProfileImage(path: String) {
        _userInputImage.value = path
        Log.e(TAG, "setUserProfileImage: ${_userInputImage.value}")
    }

    fun setUserInterestExercise(exercises: List<Int>) {
        _userInterestExercise.value = exercises
    }

    fun requestSocialSignUp(path: String) {
        viewModelScope.launch {
            signUpRepository.requestSignUpWithSocial(
                marketingAgree = _userAgreement.value!!,
                name = _userInputName.value!!,
                nickname = _userInputNickName.value!!,
                birth = _userInputBirth.value!!,
                gender = _userInputGender.value!!,
                preferExercises = _userInterestExercise.value!!,
                profileImage = path.mapToMultipart()
            )
                .onSuccess {
                    _signUpState.value = it.code
                    saveUserData(it.result.userId)
                }
                .onFailure { _signUpState.value = it.message?.toInt() }
        }
    }

    private fun String.mapToMultipart(): MultipartBody.Part {
        val file = File(this)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("profileImage", file.name, requestFile)
    }

    fun setPathForDelete(paths: Uri) {
        _imagePaths.postValue(paths)
    }

    private fun saveUserData(userId : Int) {
        viewModelScope.launch {
            signInRepository.saveUserData(userId, null)
        }
    }
}