package com.proteam.fithub.presentation.ui.sign.up.social.info

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSocialInfoBinding
import com.proteam.fithub.presentation.ui.sign.up.common.profile.UserProfileFragment
import com.proteam.fithub.presentation.ui.sign.up.social.SocialSignUpActivity
import com.proteam.fithub.presentation.ui.sign.up.social.viewmodel.SocialSignUpViewModel
import com.proteam.fithub.presentation.util.AnalyticsHelper

class SocialInfoFragment : Fragment() {
    private lateinit var binding : FragmentSocialInfoBinding
    private val viewModel : SocialSignUpViewModel by activityViewModels()
    private lateinit var imm: InputMethodManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_social_info, container, false)

        AnalyticsHelper.setAnalyticsLog(this.javaClass.simpleName)

        initBinding()
        initUi()
        initInputMethodManager()

        return binding.root
    }

    private fun initBinding() {
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initUi() {
        initComponents()
    }

    private fun initComponents() {
        binding.fgSignUpSocialEdtName.setErrorEnable(true)
        binding.fgSignUpSocialEdtBirth.setErrorEnable(true)
    }

    private fun setUserData() {
        viewModel.setUserName(binding.fgSignUpSocialEdtName.getUserInputContent())
        viewModel.setUserBirth(binding.fgSignUpSocialEdtBirth.getUserInputBirth())
        viewModel.setUserGender(binding.fgSignUpSocialEdtBirth.getUserInputGender())
    }

    fun onNextClicked() {
        setUserData()
        hideKeyboard()
        (requireActivity() as SocialSignUpActivity).changeFragments(UserProfileFragment())
    }

    private fun initInputMethodManager() {
        imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private fun hideKeyboard() {
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun nameBinding() = binding.fgSignUpSocialEdtName
    fun birthBinding() = binding.fgSignUpSocialEdtBirth
}