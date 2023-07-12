package com.proteam.fithub.presentation.ui.signup.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.R
import com.proteam.fithub.data.data.SignUpAgreement
import com.proteam.fithub.data.remote.request.RequestCheckSMSAuth
import com.proteam.fithub.data.remote.request.RequestSMSAuth
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.domain.repository.ExerciseRepository
import com.proteam.fithub.domain.repository.SignUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpRepository: SignUpRepository,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {
    private val _viewType = MutableLiveData<String>()
    val viewType : LiveData<String> = _viewType

    var signUpAgreements =
        MutableLiveData<MutableList<SignUpAgreement>>().also { it.value = agreementData() }
    var signUpAllAgreements = MutableLiveData<Boolean>()

    var agreementNextEnable = MutableLiveData<Boolean>(false)
    var selectTelecomState = MutableLiveData<Boolean>(false)

    private val _existExercises = MutableLiveData<MutableList<ResponseExercises.ExercisesList>>()
    val existExercises: LiveData<MutableList<ResponseExercises.ExercisesList>> = _existExercises

    private val _authResult = MutableLiveData<Boolean>()
    val authResult: LiveData<Boolean> = _authResult

    private val _checkNickNameResult = MutableLiveData<Int>()
    val checkNickNameResult: LiveData<Int> = _checkNickNameResult

    /** User Input **/
    private val _selectedExercises = MutableLiveData<MutableList<Int>>()
    val selectExercises: LiveData<MutableList<Int>> = _selectedExercises
    private val toolSelectInterestSports = mutableListOf<Int>()

    private val _userSelectedProfileImage =
        MutableLiveData<Any>()
    val userSelectedProfileImage: LiveData<Any> = _userSelectedProfileImage

    private val userInputPhoneNumber = MutableLiveData<String>()
    private val userInputName = MutableLiveData<String>()
    private val userInputBirth = MutableLiveData<String>()
    private val userInputNickName = MutableLiveData<String>()
    private val userInputPassword = MutableLiveData<String>()

    var selectTelecom = MutableLiveData<String>()

    /** SignIn Type **/
    fun setType(type : String) {
        _viewType.value = type
    }

    /** Init **/

    fun initUserInfo() {
        selectTelecomState.value = false
        selectTelecom = MutableLiveData()
    }

    fun initAuthResult() {
        _authResult.value = false
    }

    fun initProfile() {
        _userSelectedProfileImage.value = R.drawable.ic_sign_up_default_profile
    }

    /** Agreement **/

    fun manageAllAgreements(status: Boolean) {
        for (i in 0 until signUpAgreements.value!!.size) {
            signUpAgreements.value!![i] = signUpAgreements.value!![i].also { it.checked = status }
        }
        checkAgreementFinished()
    }

    fun onAgreementClicked() {
        checkAgreementFinished()
        checkAllAgreementsStatus()
    }

    private fun checkAllAgreementsStatus() {
        signUpAllAgreements.value =
            signUpAgreements.value?.count { it.checked } == signUpAgreements.value?.size
    }

    private fun checkAgreementFinished() {
        agreementNextEnable.value = signUpAgreements.value?.count { it.checked && it.required } == 4
    }

    /** Set User Data **/
    fun setSelectedTelecom(title: String) {
        selectTelecom.value = title
        selectTelecomState.value = true
    }

    fun addSelectInterestSports(sportsId: Int) {
        toolSelectInterestSports.add(sportsId)
        _selectedExercises.value = toolSelectInterestSports
    }

    fun removeSelectInterestSports(sportsId: Int) {
        toolSelectInterestSports.remove(sportsId)
        _selectedExercises.value = toolSelectInterestSports
    }

    fun setUserSelectedProfile(path: Any) {
        _userSelectedProfileImage.value = path
    }

    fun setUserNickname(nickName : String) {
        userInputNickName.value = nickName
    }
    fun setUserPassword(password : String) {
        userInputPassword.value = password
    }
    fun setUserName(name : String) {
        userInputName.value = name
    }
    fun setUserBirth(birth : String) {
        userInputBirth.value = birth
    }

    fun setUserPhoneNumber(number: String) {
        userInputPhoneNumber.value = number
    }

    /** Request **/

    fun requestSMSAuthCode() {
        viewModelScope.launch {
            signUpRepository.requestSMSAuth(RequestSMSAuth(userInputPhoneNumber.value!!))
                .onSuccess { }
        }
    }

    fun requestCheckSMSAuthCode(userAuthCode: String) {
        viewModelScope.launch {
            signUpRepository.requestCheckSMSAuth(
                RequestCheckSMSAuth(
                    userInputPhoneNumber.value!!,
                    userAuthCode.toInt()
                )
            )
                .onSuccess { _authResult.value = it.code == 2000 }
        }
    }

    fun requestCheckSameNickName(nickname: String) {
        viewModelScope.launch {
            signUpRepository.requestCheckSameNickName(nickname)
                .onSuccess { _checkNickNameResult.value = it.code }
        }
    }

    fun requestExercises() {
        viewModelScope.launch {
            exerciseRepository.requestExercises()
                .onSuccess { _existExercises.value = it.categoryList as MutableList}
        }
    }

    fun requestSignUpWithPhone() {
        if(viewType.value == "Phone") {
            //폰번호로 회원가입
        } else {
            //소셜 회원가입
        }
        Log.d("----", "showAllUserInputData: ${signUpAgreements.value?.count { it.checked } == 5} / ${userInputPhoneNumber.value} / ${userInputPassword.value} / ${userInputName.value} / ${userInputNickName.value} / ${userInputBirth.value} / ${selectExercises.value}")
    }

    /** Dummy **/
    fun agreementData(): MutableList<SignUpAgreement> = mutableListOf(
        SignUpAgreement("(필수) 개인정보 수집 및 이용에 동의합니다.", false, true, "123"),
        SignUpAgreement("(필수) 이용약관에 동의합니다.", false, true, "123"),
        SignUpAgreement("(필수) 위치 기반 서비스 약관에 동의합니다.", false, true, "123"),
        SignUpAgreement("(필수) 만 14세 이상 입니다.", false, true, "123"),
        SignUpAgreement("(선택) 마케팅 정보 수신에 동의합니다.", false, false, "123")
    )

}