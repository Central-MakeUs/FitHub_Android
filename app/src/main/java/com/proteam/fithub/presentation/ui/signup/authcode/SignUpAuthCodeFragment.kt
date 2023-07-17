package com.proteam.fithub.presentation.ui.signup.authcode

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSignUpAuthCodeBinding
import com.proteam.fithub.presentation.component.ComponentAlertToast
import com.proteam.fithub.presentation.ui.findpassword.FindPasswordActivity
import com.proteam.fithub.presentation.ui.findpassword.viewmodel.FindPasswordViewModel
import com.proteam.fithub.presentation.ui.signup.SignUpActivity
import com.proteam.fithub.presentation.ui.signup.password.SignUpSetPasswordFragment
import com.proteam.fithub.presentation.ui.signup.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpAuthCodeFragment : Fragment() {
    private lateinit var binding : FragmentSignUpAuthCodeBinding
    private lateinit var imm: InputMethodManager

    private val findPasswordViewModel : FindPasswordViewModel by activityViewModels()
    private val signUpViewModel : SignUpViewModel by activityViewModels()

    private val alertToast = ComponentAlertToast()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_auth_code, container, false)

        validateTAG()

        initInputMethodManager()
        initBinding()
        initInclude()
        initObserve()

        return binding.root
    }

    private fun validateTAG() {
        when(tag) {
            "Sign_Up" -> {}
            "Find_Password" -> {}
        }
    }

    private fun requestAuthCode() {
        when(tag) {
            "Find_Password" -> { findPasswordViewModel.requestSMSAuthCode() }
        }

    }

    private fun initBinding() {
        binding.fragment = this
    }

    private fun initInclude() {
        binding.fgSignUpAuthCodeEdtAuthCode.apply {
            authCodeEdt.requestFocus()
            startCountDownTimer()
            requestAuthCode()
        }
    }

    private fun initObserve() {
        binding.fgSignUpAuthCodeEdtAuthCode.isFinished.observe(viewLifecycleOwner) {
            binding.fgSignUpAuthCodeBtnNext.isEnabled = it
        }
    }

    private fun initInputMethodManager() {
        imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onResume() {
        super.onResume()
        binding.fgSignUpAuthCodeEdtAuthCode.authCodeEdt.showKeyboard()
    }

    fun onResendClicked() {
        binding.fgSignUpAuthCodeEdtAuthCode.apply {
            stopCountDownTimer()
            startCountDownTimer()
            requestAuthCode()
        }
    }

    private fun EditText.showKeyboard() = imm.showSoftInput(this, 0)

    private fun hideKeyboard() {
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun onNextBtnClicked() {
        hideKeyboard()
        checkRequestCode()
        when(tag) {
            "Sign_Up" -> resultActWhenSignUp()
            "Find_Password" -> resultActWhenChangePassword()
        }
    }

    private fun checkRequestCode() {
        when(tag) {
            "Sign_Up" -> { signUpViewModel.requestCheckSMSAuthCode(binding.fgSignUpAuthCodeEdtAuthCode.getUserInputContent()) }
            "Find_Password" -> { findPasswordViewModel.requestCheckSMSAuthCode(binding.fgSignUpAuthCodeEdtAuthCode.getUserInputContent()) }
        }
    }

    private fun resultActWhenSignUp() {
        signUpViewModel.authResult.observe(viewLifecycleOwner) {
            if(it == 2000) {
                (requireActivity() as SignUpActivity).changeFragments(SignUpSetPasswordFragment())
                signUpViewModel.initAuthResult()
            } else if(it in 4000..4999){
                checkWhenNumberAuthFailed(it)
            }
        }
    }

    private fun resultActWhenChangePassword() {
        findPasswordViewModel.authResult.observe(viewLifecycleOwner) {
            if(it == 2000) {
                (requireActivity() as FindPasswordActivity).changeFragments(SignUpSetPasswordFragment())
                findPasswordViewModel.initAuthResult()
            } else if(it in 4000..4999) {
                checkWhenNumberAuthFailed(it)
            }
        }
    }

    private fun checkWhenNumberAuthFailed(code : Int) {
        binding.fgSignUpAuthCodeBtnNext.isEnabled = false
        alertToast.show(requireActivity().supportFragmentManager, code.toString())
    }
}