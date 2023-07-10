package com.proteam.fithub.presentation.ui.signup.password

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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

class SignUpSetPasswordFragment : Fragment() {
    private lateinit var binding : FragmentSignUpSetPasswordBinding

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
        binding.fgSignUpSetPasswordEdtPassword.getAttr(true, false)
        binding.fgSignUpSetPasswordEdtPasswordCheck.getAttr(true, true)
    }

    private fun initIncludeObserve() {
        binding.fgSignUpSetPasswordEdtPassword.userInputPassword.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()) {
                binding.fgSignUpSetPasswordEdtPasswordCheck.getPassword(it)
                binding.fgSignUpSetPasswordEdtPasswordCheck.checkSame()
            }
        }
    }

    private fun initNextBtnEnableObserve() {
        binding.fgSignUpSetPasswordEdtPassword.doneState.observe(viewLifecycleOwner) {
            binding.fgSignUpSetPasswordBtnNext.isEnabled = it && binding.fgSignUpSetPasswordEdtPasswordCheck.doneState.value == true
        }
        binding.fgSignUpSetPasswordEdtPasswordCheck.doneState.observe(viewLifecycleOwner) {
            binding.fgSignUpSetPasswordBtnNext.isEnabled = it && binding.fgSignUpSetPasswordEdtPassword.doneState.value == true
        }
    }

    fun onNextClicked() {
        when(tag) {
            "Find_Password" -> changePasswords()
            "Sign_Up" -> (requireActivity() as SignUpActivity).changeFragments(SignUpUserProfileFragment())
        }
    }

    private fun changePasswords() {
        ComponentDialogYesNo(::showSignIn).show(requireActivity().supportFragmentManager, "RESET_PASSWORD")
    }

    private fun showSignIn() {
        startActivity(Intent(requireActivity(), SignInWithPhoneNumberActivity::class.java))
        requireActivity().finish()
    }

}