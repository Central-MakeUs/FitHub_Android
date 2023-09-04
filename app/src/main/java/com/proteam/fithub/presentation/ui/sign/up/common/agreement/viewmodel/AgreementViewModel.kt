package com.proteam.fithub.presentation.ui.sign.up.common.agreement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.data.data.Agreements
import com.proteam.fithub.data.remote.response.ResponseTermsData
import com.proteam.fithub.domain.repository.MyPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgreementViewModel @Inject constructor(private val myPageRepository: MyPageRepository): ViewModel() {
    var signUpAgreements =
        MutableLiveData<MutableList<Agreements>>().also { it.value = agreementData() }
    var signUpAllAgreements = MutableLiveData<Boolean>()

    var agreementNextEnable = MutableLiveData<Boolean>(false)

    private val _termsData = MutableLiveData<ResponseTermsData.ResultTermsData>()
    val termsData : LiveData<ResponseTermsData.ResultTermsData> = _termsData

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

    fun returnUserAgreeResult() = signUpAgreements.value?.count { it.checked } == 5

    fun requestTermData(position : Int) {
        viewModelScope.launch {
            myPageRepository.requestTermsData(position)
                .onSuccess { _termsData.value = it }
        }
    }

    /** Dummy **/
    fun agreementData(): MutableList<Agreements> = mutableListOf(
        Agreements("(필수) 개인정보 수집 및 이용에 동의합니다.", false, true, "123"),
        Agreements("(필수) 이용약관에 동의합니다.", false, true, "123"),
        Agreements("(필수) 위치 기반 서비스 약관에 동의합니다.", false, true, "123"),
        Agreements("(필수) 만 14세 이상 입니다.", false, true, "123"),
        Agreements("(선택) 마케팅 정보 수신에 동의합니다.", false, false, "123")
    )
}