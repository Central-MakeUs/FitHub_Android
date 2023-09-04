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

    private val _userAgreement = MutableLiveData<Boolean>()
    private val _userInputName = MutableLiveData<String>()
    private val _userInputBirth = MutableLiveData<String>()
    private val _userInputGender = MutableLiveData<String>()
    private val _userInputNickName = MutableLiveData<String>()
    private val _userInputImage = MutableLiveData<String>()
    private val _userInterestExercise = MutableLiveData<Int>()

    val userInputImage: LiveData<String> = _userInputImage
    val userInputNickName : LiveData<String> = _userInputNickName

    private val _imagePaths = MutableLiveData<Uri>()
    val imagePaths: LiveData<Uri> = _imagePaths

    private val _signUpState = MutableLiveData<Int>()
    val signUpState: LiveData<Int> = _signUpState

    /** Set Data **/
    fun setUserAgreements(agree: Boolean) {
        _userAgreement.value = agree
    }

    fun setUserName(name: String) {
        _userInputName.value = name
    }

    fun setUserBirth(birth: String) {
        _userInputBirth.value = birth
    }

    fun setUserGender(gender: String) {
        _userInputGender.value = gender
    }

    fun setUserNickName(nickname: String) {
        _userInputNickName.value = nickname
    }

    fun setUserProfileImage(path: String) {
        _userInputImage.value = path
    }

    fun setUserInterestExercise(exercise: Int) {
        _userInterestExercise.value = exercise
    }

    /** Request **/
    fun requestSocialSignUp(path: String?, token : String) {
        viewModelScope.launch {
            signUpRepository.requestSignUpWithSocial(
                marketingAgree = _userAgreement.value!!,
                name = _userInputName.value!!,
                nickname = _userInputNickName.value!!,
                birth = _userInputBirth.value!!,
                gender = _userInputGender.value!!,
                preferExercises = _userInterestExercise.value!!,
                profileImage = path?.mapToMultipart(),
                fcmToken = token
            )
                .onSuccess {
                    _signUpState.value = it.code
                    saveUserData(it.result.userId)
                }
                .onFailure { _signUpState.value = it.message?.toInt() }
        }
    }

    /** MultiPart **/
    private fun String?.mapToMultipart(): MultipartBody.Part? {
        return if(this == null) {
            return null
        } else {
            val file = File(this)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("profileImage", file.name, requestFile)
        }
    }

    fun setPathForDelete(paths: Uri) {
        _imagePaths.postValue(paths)
    }

    /** Local **/
    private fun saveUserData(userId : Int) {
        viewModelScope.launch {
            signInRepository.saveUserData(userId, null)
        }
    }
}