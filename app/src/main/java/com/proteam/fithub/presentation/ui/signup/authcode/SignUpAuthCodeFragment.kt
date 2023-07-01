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
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSignUpAuthCodeBinding

class SignUpAuthCodeFragment : Fragment() {
    private lateinit var binding : FragmentSignUpAuthCodeBinding
    private lateinit var imm: InputMethodManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_auth_code, container, false)

        initInputMethodManager()
        initBinding()
        initInclude()
        initObserve()

        return binding.root
    }

    private fun initBinding() {
        binding.fragment = this
    }

    private fun initInclude() {
        binding.fgSignUpAuthCodeEdtAuthCode.apply {
            authCodeEdt.requestFocus()
            startCountDownTimer()
        }
    }

    private fun initObserve() {
        binding.fgSignUpAuthCodeEdtAuthCode.doneState.observe(viewLifecycleOwner) {
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

    private fun EditText.showKeyboard() = imm.showSoftInput(this, 0)

    private fun hideKeyboard() {
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun onNextBtnClicked() {
        hideKeyboard()
    }
}