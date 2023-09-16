package com.proteam.fithub.presentation.ui.sign.up.common.authcode

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
import androidx.fragment.app.viewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentAuthCodeBinding
import com.proteam.fithub.presentation.util.LoadingDialog
import com.proteam.fithub.presentation.ui.findpassword.FindPasswordActivity
import com.proteam.fithub.presentation.ui.findpassword.viewmodel.FindPasswordViewModel
import com.proteam.fithub.presentation.ui.sign.up.common.authcode.viewmodel.AuthCodeViewModel
import com.proteam.fithub.presentation.ui.sign.up.common.password.PasswordFragment
import com.proteam.fithub.presentation.ui.sign.up.number.NumberSignUpActivity
import com.proteam.fithub.presentation.ui.sign.up.number.viewmodel.NumberSignUpViewModel
import com.proteam.fithub.presentation.util.AnalyticsHelper
import com.proteam.fithub.presentation.util.CustomSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthCodeFragment : Fragment() {
    private lateinit var binding: FragmentAuthCodeBinding
    private lateinit var imm: InputMethodManager

    private val viewModel: AuthCodeViewModel by viewModels()
    private val numberViewModel: NumberSignUpViewModel by activityViewModels()

    private val findPasswordViewModel: FindPasswordViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auth_code, container, false)

        AnalyticsHelper.setAnalyticsLog(this.javaClass.simpleName)

        initInputMethodManager()
        initBinding()
        initUi()
        initObserve()

        return binding.root
    }

    private fun requestAuthCode() {
        when (tag) {
            /** 휴대폰번호 가입 **/
            "Number" -> {
                viewModel.requestSMSAuthCode(numberViewModel.userInputPhoneNumber.value!!)
            }

            "Find" -> {
                viewModel.requestSMSAuthCode(findPasswordViewModel.userInputPhoneNumber.value!!)
            }
        }
    }

    private fun initBinding() {
        binding.fragment = this
    }

    private fun initUi() {
        initComponent()
        requestAuthCode()
    }

    private fun initComponent() {
        binding.fgSignUpAuthCodeEdtAuthCode.apply {
            authCodeEdt.requestFocus()
            startCountDownTimer()
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
        checkRequestCode()
    }


    private fun checkRequestCode() {
        showLoadingDialog()
        when (tag) {
            "Number" -> {
                viewModel.requestCheckSMSAuthCode(
                    numberViewModel.userInputPhoneNumber.value!!,
                    binding.fgSignUpAuthCodeEdtAuthCode.getUserInputContent()
                )
                resultActWhenSignUp()
            }

            "Find" -> {
                viewModel.requestCheckSMSAuthCode(
                    findPasswordViewModel.userInputPhoneNumber.value!!,
                    binding.fgSignUpAuthCodeEdtAuthCode.getUserInputContent()
                )
                resultActWhenChangePassword()
            }
        }
    }

    private fun resultActWhenSignUp() {
        viewModel.authResult.observe(viewLifecycleOwner) {
            dismissLoadingDialog()
            if (it == 2000) {
                (requireActivity() as NumberSignUpActivity).changeFragments(PasswordFragment())
                viewModel.initAuthResult()
            } else if (it in 4000..4999) {
                checkWhenNumberAuthFailed(it)
            }
        }
    }

    private fun resultActWhenChangePassword() {
        viewModel.authResult.observe(viewLifecycleOwner) {
            if (it == 2000) {
                (requireActivity() as FindPasswordActivity).changeFragments(
                    PasswordFragment()
                )
            } else if (it in 4000..4999) {
                checkWhenNumberAuthFailed(it)
            }
            viewModel.initAuthResult()
        }
    }

    private fun checkWhenNumberAuthFailed(code: Int) {
        binding.fgSignUpAuthCodeBtnNext.isEnabled = false
        CustomSnackBar.makeSnackBar(binding.root, code.toString())
    }

    private var loadingDialog = LoadingDialog()
    private fun showLoadingDialog() = loadingDialog.show(requireActivity().supportFragmentManager, null)
    private fun dismissLoadingDialog() = loadingDialog.dismiss()
}