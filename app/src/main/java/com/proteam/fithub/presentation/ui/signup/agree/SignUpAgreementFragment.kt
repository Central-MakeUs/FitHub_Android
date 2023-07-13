package com.proteam.fithub.presentation.ui.signup.agree

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSignUpAgreementBinding
import com.proteam.fithub.presentation.ui.signup.SignUpActivity
import com.proteam.fithub.presentation.ui.signup.agree.adapter.SignUpAgreeAdapter
import com.proteam.fithub.presentation.ui.signup.interest.SelectInterestSportsFragment
import com.proteam.fithub.presentation.ui.signup.password.SignUpSetPasswordFragment
import com.proteam.fithub.presentation.ui.signup.phone.SignUpPhoneNumberFragment
import com.proteam.fithub.presentation.ui.signup.profile.SignUpUserProfileFragment
import com.proteam.fithub.presentation.ui.signup.viewmodel.SignUpViewModel

class SignUpAgreementFragment : Fragment() {
    private lateinit var binding : FragmentSignUpAgreementBinding
    private val viewModel : SignUpViewModel by activityViewModels()

    private val adapter by lazy {
        SignUpAgreeAdapter(::onAgreementClicked)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_agreement, container, false)

        initBinding()
        initAgreeRV()


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        observeTest()
    }

    private fun initBinding() {
        binding.fragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initAgreeRV() {
        binding.fgSignUpAgreementRvContents.adapter = adapter
    }

    fun onAllAgreementClicked() {
        binding.fgSignUpAgreementCheckAll.isChecked.apply {
            viewModel.manageAllAgreements(this)
            adapter.setAllClicked(this)
        }
    }

    private fun observeTest() {
        viewModel.signUpAgreements.observe(viewLifecycleOwner) {
            adapter.agreements = it
        }
    }

    private fun onAgreementClicked() {
        viewModel.onAgreementClicked()
    }

    fun onNextBtnClicked() {
        (requireActivity() as SignUpActivity).changeFragments(SignUpPhoneNumberFragment())
    }

}