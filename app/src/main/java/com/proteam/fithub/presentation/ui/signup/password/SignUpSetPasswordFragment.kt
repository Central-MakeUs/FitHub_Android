package com.proteam.fithub.presentation.ui.signup.password

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
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.sign_up_layout_container, SignUpUserProfileFragment())
            .addToBackStack("Profile").commit()
    }

}