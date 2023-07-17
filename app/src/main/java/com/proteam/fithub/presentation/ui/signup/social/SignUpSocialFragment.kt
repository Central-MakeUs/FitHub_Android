package com.proteam.fithub.presentation.ui.signup.social

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSignUpSocialBinding
import com.proteam.fithub.presentation.ui.signup.SignUpActivity
import com.proteam.fithub.presentation.ui.signup.profile.SignUpUserProfileFragment

class SignUpSocialFragment : Fragment() {
    private lateinit var binding : FragmentSignUpSocialBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_social, container, false)

        initBinding()
        initInclude()
        checkNextEnable()

        return binding.root
    }

    private fun initBinding() {
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initInclude() {
        binding.fgSignUpSocialEdtName.setErrorEnable(true)
        binding.fgSignUpSocialEdtBirth.setErrorEnable(true)
    }

    private fun checkNextEnable() {
        binding.fgSignUpSocialEdtName.isFinished.observe(viewLifecycleOwner) {
            binding.fgSignUpSocialBtnNext.isEnabled = it && binding.fgSignUpSocialEdtBirth.isFinished.value == true
        }
        binding.fgSignUpSocialEdtBirth.isFinished.observe(viewLifecycleOwner) {
            binding.fgSignUpSocialBtnNext.isEnabled = it && binding.fgSignUpSocialEdtName.isFinished.value == true
        }
    }

    fun onNextClicked() {
        (requireActivity() as SignUpActivity).changeFragments(SignUpUserProfileFragment())
    }
}