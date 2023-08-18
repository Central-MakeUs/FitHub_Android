package com.proteam.fithub.presentation.ui.sign.up.number.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class NumberSignUpViewModel @Inject constructor(
    private val signUpRepository: SignUpRepository,
    private val signInRepository: SignInRepository) :
    ViewModel() {

    private val _userAgreement = MutableLiveData<Boolean>()
    private val _userInputPhoneNumber = MutableLiveData<String>()
    private val _userInputName = MutableLiveData<String>()
    private val _userInputBirth = MutableLiveData<String>()
    private val _userInputGender = MutableLiveData<String>()
    private val _userInputPassword = MutableLiveData<String>()
    private val _userInputNickname = MutableLiveData<String>()
    private val _userInputProfileImage = MutableLiveData<String>()
    private val _userInterestExercise = MutableLiveData<Int>()

    var selectTelecom = MutableLiveData<String>()
    var selectTelecomState = MutableLiveData(false)

    val userInputNickname: LiveData<String> = _userInputNickname
    val userInputPhoneNumber: LiveData<String> = _userInputPhoneNumber
    val userInputProfileImage: LiveData<String> = _userInputProfileImage

    private val _imagePaths = MutableLiveData<Uri>()
    val imagePaths: LiveData<Uri> = _imagePaths

    private val _signUpState = MutableLiveData<Int>()
    val signUpState: LiveData<Int> = _signUpState

    /** SET DATA **/
    fun setUserAgreements(agree: Boolean) {
        _userAgreement.value = agree
    }

    fun setUserPhoneNumber(phoneNumber: String) {
        _userInputPhoneNumber.value = phoneNumber
    }

    fun setSelectedTelecom(title: String) {
        selectTelecom.value = title
        selectTelecomState.value = true
    }

    fun initTelecom() {
        selectTelecom.value = ""
        selectTelecomState.value = false
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

    fun setUserPassword(password: String) {
        _userInputPassword.value = password
    }

    fun setUserNickname(nickname: String) {
        _userInputNickname.value = nickname
    }

    fun setUserProfileImage(profileImage: String) {
        _userInputProfileImage.value = profileImage
    }

    fun setUserInterestExercise(exercise: Int) {
        _userInterestExercise.value = exercise
    }

    fun initState() {
        _signUpState.value = 0
    }

    fun requestNumberSignUp(path: String?, token : String) {
        viewModelScope.launch {
            signUpRepository.requestSignUpWithPhone(
                marketingAgree = _userAgreement.value!!,
                phoneNumber = _userInputPhoneNumber.value!!,
                name = _userInputName.value!!,
                nickname = _userInputNickname.value!!,
                birth = _userInputBirth.value!!,
                gender = _userInputGender.value!!,
                password = _userInputPassword.value!!,
                preferExercises = _userInterestExercise.value!!,
                profileImage = path?.mapToMultipart(),
                fcmToken = token
            )
                .onSuccess {
                _signUpState.value = it.code
                saveUserData(it.result.userId, it.result.accessToken)
            }
                .onFailure { _signUpState.value = it.message?.toInt() }
        }
    }

    /** MultiPart **/
    private fun String?.mapToMultipart(): MultipartBody.Part? {
        return if (this == null) {
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
    private fun saveUserData(userId: Int, token : String) {
        viewModelScope.launch {
            signInRepository.saveUserData(userId, token)
        }
    }
}