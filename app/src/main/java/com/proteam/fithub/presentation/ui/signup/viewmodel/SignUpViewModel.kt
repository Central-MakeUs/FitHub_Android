package com.proteam.fithub.presentation.ui.signup.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proteam.fithub.data.data.SignUpAgreement

class SignUpViewModel : ViewModel() {
    var signUpAgreements = MutableLiveData<MutableList<SignUpAgreement>>().also { it.value = agreementData() }
    var signUpAllAgreements = MutableLiveData<Boolean>()
    var agreementNextEnable = MutableLiveData<Boolean>(false)

    fun manageAllAgreements(status : Boolean) {
        for (i in 0 until signUpAgreements.value!!.size) {
            signUpAgreements.value!![i] = signUpAgreements.value!![i].also { it.checked = status }
        }
        checkAgreementFinished()
    }

    fun onAgreementClicked() {
        checkAgreementFinished()
    }

    private fun checkAgreementFinished() {
        agreementNextEnable.value = signUpAgreements.value?.count { it.checked && it.required } == 4
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