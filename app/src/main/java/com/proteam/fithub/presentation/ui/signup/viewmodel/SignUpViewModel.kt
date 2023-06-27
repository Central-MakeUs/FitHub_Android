package com.proteam.fithub.presentation.ui.signup.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {

    var signUpAgreements = mutableListOf<MutableLiveData<Boolean>>().also { for(i in 0 until 5) it.add(MutableLiveData(false)) }
    var signUpAllAgreements = MutableLiveData<Boolean>(false)

    fun manageAllAgreements(status : Boolean) {
        signUpAgreements.forEach { it.value = status }

        for(i in signUpAgreements) {
            Log.d("----", "manageAllAgreements: ${i.value}")
        }
    }

}