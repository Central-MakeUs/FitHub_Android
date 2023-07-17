package com.proteam.fithub.presentation.ui.signup.password

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSignUpSetPasswordBinding
import com.proteam.fithub.presentation.component.ComponentAlertToast
import com.proteam.fithub.presentation.component.ComponentDialogOneButton
import com.proteam.fithub.presentation.ui.auth.signinphone.SignInWithPhoneNumberActivity
import com.proteam.fithub.presentation.ui.findpassword.viewmodel.FindPasswordViewModel
import com.proteam.fithub.presentation.ui.signup.SignUpActivity
import com.proteam.fithub.presentation.ui.signup.profile.SignUpUserProfileFragment
import com.proteam.fithub.presentation.ui.signup.viewmodel.SignUpViewModel

class SignUpSetPasswordFragment : Fragment() {
    private lateinit var binding : FragmentSignUpSetPasswordBinding
    private val signUpViewModel : SignUpViewModel by activityViewModels()
    private val findPasswordViewModel : FindPasswordViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_set_password, container, false)


        initBinding()
        initInclude()
        initIncludeObserve()
        initNextBtnEnableObserve()

        return binding.root
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
        findPasswordViewModel.requestChangePassword(binding.fgSignUpSetPasswordEdtPassword.returnUserInputContent())
        observePasswordResult()
    }

    private fun observePasswordResult() {
        findPasswordViewModel.passwordResult.observe(viewLifecycleOwner) {
            if(it == 2000) ComponentDialogOneButton(::showSignIn).show(requireActivity().supportFragmentManager, "RESET_PASSWORD")
            else ComponentAlertToast().show(requireActivity().supportFragmentManager, it.toString())
        }
    }

    private fun showSignIn() {
        requireActivity().finish()
    }

}