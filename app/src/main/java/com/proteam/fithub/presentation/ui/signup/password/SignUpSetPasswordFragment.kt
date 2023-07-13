package com.proteam.fithub.presentation.ui.signup.password

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSignUpSetPasswordBinding
import com.proteam.fithub.presentation.component.ComponentDialogYesNo
import com.proteam.fithub.presentation.ui.auth.SignInActivity
import com.proteam.fithub.presentation.ui.auth.SignInWithPhoneNumberActivity
import com.proteam.fithub.presentation.ui.findpassword.FindPasswordActivity
import com.proteam.fithub.presentation.ui.signup.SignUpActivity
import com.proteam.fithub.presentation.ui.signup.authcode.SignUpAuthCodeFragment
import com.proteam.fithub.presentation.ui.signup.profile.SignUpUserProfileFragment
import com.proteam.fithub.presentation.ui.signup.viewmodel.SignUpViewModel

class SignUpSetPasswordFragment : Fragment() {
    private lateinit var binding : FragmentSignUpSetPasswordBinding
    private val signUpViewModel : SignUpViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_set_password, container, false)

        validateTag()

        initBinding()
        initInclude()
        initIncludeObserve()
        initNextBtnEnableObserve()

        return binding.root
    }

    private fun validateTag() {

    }

    private fun initBinding() {
        binding.fragment = this
    }

    private fun initInclude() {
        binding.fgSignUpSetPasswordEdtPassword.setErrorEnable(true, false)
        binding.fgSignUpSetPasswordEdtPasswordCheck.setErrorEnable(true, true)
    }

    private fun initIncludeObserve() {
        binding.fgSignUpSetPasswordEdtPassword.userInputPassword.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()) {
                binding.fgSignUpSetPasswordEdtPasswordCheck.setPasswordForCheck(it)
                binding.fgSignUpSetPasswordEdtPasswordCheck.checkWhenOriginalChanged()
            }
        }
    }

    private fun initNextBtnEnableObserve() {
        binding.fgSignUpSetPasswordEdtPassword.isFinished.observe(viewLifecycleOwner) {
            binding.fgSignUpSetPasswordBtnNext.isEnabled = it && binding.fgSignUpSetPasswordEdtPasswordCheck.isFinished.value == true
        }
        binding.fgSignUpSetPasswordEdtPasswordCheck.isFinished.observe(viewLifecycleOwner) {
            binding.fgSignUpSetPasswordBtnNext.isEnabled = it && binding.fgSignUpSetPasswordEdtPassword.isFinished.value == true
        }
    }

    fun onNextClicked() {
        when(tag) {
            "Find_Password" -> changePasswords()
            "Sign_Up" -> {
                setUserPassword()
                (requireActivity() as SignUpActivity).changeFragments(SignUpUserProfileFragment())
            }
        }
    }

    private fun setUserPassword() {
        signUpViewModel.setUserPassword(binding.fgSignUpSetPasswordEdtPassword.returnUserInputContent())
    }

    private fun changePasswords() {
        ComponentDialogYesNo(::showSignIn).show(requireActivity().supportFragmentManager, "RESET_PASSWORD")
    }

    private fun showSignIn() {
        startActivity(Intent(requireActivity(), SignInWithPhoneNumberActivity::class.java))
        requireActivity().finish()
    }

}