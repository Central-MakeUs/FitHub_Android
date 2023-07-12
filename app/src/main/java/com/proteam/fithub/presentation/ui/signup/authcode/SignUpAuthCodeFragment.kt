package com.proteam.fithub.presentation.ui.signup.authcode

import android.content.Context
import android.os.Bundle
import android.util.Log
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
            "SignUp" -> {}
            "Find_Password" -> {}
        }
    }

    private fun requestAuthCode() {
        signUpViewModel.requestSMSAuthCode()
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
        signUpViewModel.requestCheckSMSAuthCode(binding.fgSignUpAuthCodeEdtAuthCode.getUserInputContent())
        signUpViewModel.authResult.observe(viewLifecycleOwner) {
            Log.d("----", "onNextBtnClicked: $it")
            if(it) {
                when(tag) {
                    "Find_Password" -> (requireActivity() as FindPasswordActivity).changeFragments(SignUpSetPasswordFragment())
                    "Sign_Up" -> (requireActivity() as SignUpActivity).changeFragments(SignUpSetPasswordFragment())
                }
                signUpViewModel.initAuthResult()
            } else {
                //:TODO 에러코드 받아서 추가하기
            }
        }
    }
}