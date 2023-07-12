package com.proteam.fithub.presentation.ui.signup.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.R
import com.proteam.fithub.data.data.SignUpAgreement
import com.proteam.fithub.data.data.SignUpInterestSports
import com.proteam.fithub.data.remote.request.RequestCheckSMSAuth
import com.proteam.fithub.data.remote.request.RequestSMSAuth
import com.proteam.fithub.domain.repository.SignUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val signUpRepository: SignUpRepository): ViewModel() {
    var signUpAgreements = MutableLiveData<MutableList<SignUpAgreement>>().also { it.value = agreementData() }
    var signUpAllAgreements = MutableLiveData<Boolean>()

    var agreementNextEnable = MutableLiveData<Boolean>(false)
    var selectTelecomState = MutableLiveData<Boolean>(false)
    var selectTelecom = MutableLiveData<String>()

    private val _selectInterestSports = MutableLiveData<MutableList<SignUpInterestSports>>()
    val selectInterestSports : LiveData<MutableList<SignUpInterestSports>> = _selectInterestSports
    private val toolSelectInterestSports = mutableListOf<SignUpInterestSports>()

    private val _userSelectedProfileImage = MutableLiveData<Any>().apply { value = R.drawable.ic_sign_up_default_profile }
    val userSelectedProfileImage : LiveData<Any> = _userSelectedProfileImage

    private val _userInputPhoneNumber = MutableLiveData<String>()
    val userInputPhoneNumber : LiveData<String> = _userInputPhoneNumber

    private val _authResult = MutableLiveData<Boolean>()
    val authResult : LiveData<Boolean> = _authResult

    private val _checkNickNameResult = MutableLiveData<Int>()
    val checkNickNameResult : LiveData<Int> = _checkNickNameResult

    fun initUserInfo() {
        selectTelecomState.value = false
        selectTelecom = MutableLiveData()
    }

    fun initAuthResult() {
        _authResult.value = false
    }

    fun manageAllAgreements(status : Boolean) {
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
        signUpAllAgreements.value = signUpAgreements.value?.count { it.checked } == signUpAgreements.value?.size
    }

    private fun checkAgreementFinished() {
        agreementNextEnable.value = signUpAgreements.value?.count { it.checked && it.required } == 4
    }

    fun setSelectedTelecom(title : String) {
        selectTelecom.value = title
        selectTelecomState.value = true
    }

    fun addSelectInterestSports(item : SignUpInterestSports) {
        toolSelectInterestSports.add(item)
        _selectInterestSports.value = toolSelectInterestSports
    }

    fun removeSelectInterestSports(item : SignUpInterestSports) {
        toolSelectInterestSports.remove(item)
        _selectInterestSports.value = toolSelectInterestSports
    }

    fun setUserSelectedProfile(path : Any) {
        _userSelectedProfileImage.value = path
    }

    fun setUserPhoneNumber(number : String) {
        _userInputPhoneNumber.value = number
    }

    fun requestSMSAuthCode() {
        viewModelScope.launch {
            signUpRepository.requestSMSAuth(RequestSMSAuth( userInputPhoneNumber.value!!))
                .onSuccess {  }
        }
    }

    fun requestCheckSMSAuthCode(userAuthCode : String) {
        viewModelScope.launch {
            signUpRepository.requestCheckSMSAuth(RequestCheckSMSAuth(userInputPhoneNumber.value!!, userAuthCode.toInt()))
                .onSuccess {_authResult.value = it.code == 2000 }
        }
    }

    fun requestCheckSameNickName(nickname : String) {
        viewModelScope.launch {
            signUpRepository.requestCheckSameNickName(nickname)
                .onSuccess { _checkNickNameResult.value = it.code }
        }
    }

    /** Dummy **/
    fun agreementData() : MutableList<SignUpAgreement> = mutableListOf(
        SignUpAgreement("(필수) 개인정보 수집 및 이용에 동의합니다.", false, true, "123"),
        SignUpAgreement("(필수) 이용약관에 동의합니다.", false, true, "123"),
        SignUpAgreement("(필수) 위치 기반 서비스 약관에 동의합니다.", false, true, "123"),
        SignUpAgreement("(필수) 만 14세 이상 입니다.", false, true, "123"),
        SignUpAgreement("(선택) 마케팅 정보 수신에 동의합니다.", false, false, "123")
    )

}