package com.proteam.fithub.presentation.ui.sign.up.common.agreement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentAgreementBinding
import com.proteam.fithub.presentation.ui.sign.up.common.agreement.adapter.AgreementAdapter
import com.proteam.fithub.presentation.ui.sign.up.common.agreement.viewmodel.AgreementViewModel
import com.proteam.fithub.presentation.ui.sign.up.social.SocialSignUpActivity
import com.proteam.fithub.presentation.ui.sign.up.social.info.SocialInfoFragment
import com.proteam.fithub.presentation.ui.sign.up.social.viewmodel.SocialSignUpViewModel
import com.proteam.fithub.presentation.ui.signup.SignUpActivity
import com.proteam.fithub.presentation.ui.signup.phone.SignUpPhoneNumberFragment
import com.proteam.fithub.presentation.ui.signup.social.SignUpSocialFragment

class AgreementFragment : Fragment() {
    private lateinit var binding : FragmentAgreementBinding
    private val viewModel : AgreementViewModel by viewModels()

    private val socialViewModel : SocialSignUpViewModel by activityViewModels()

    private val adapter by lazy {
        AgreementAdapter(::onAgreementClicked)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_agreement, container, false)

        initBinding()
        initUi()

        return binding.root
    }



    override fun onResume() {
        super.onResume()
        observeAgreements()
    }

    private fun initBinding() {
        binding.fragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initUi() {
        initAgreements()
    }

    private fun initAgreements() {
        binding.fgSignUpAgreementRvContents.adapter = adapter
    }

    private fun onAgreementClicked() {
        viewModel.onAgreementClicked()
    }

    private fun observeAgreements() {
        viewModel.signUpAgreements.observe(viewLifecycleOwner) {
            adapter.agreements = it
        }
    }

    fun onAllAgreementClicked() {
        binding.fgSignUpAgreementCheckAll.isChecked.apply {
            viewModel.manageAllAgreements(this)
            adapter.setAllClicked(this)
        }
    }

    fun onNextBtnClicked() {
        when(tag) {
            "Phone" -> {
                (requireActivity() as SignUpActivity).changeFragments(SignUpPhoneNumberFragment())

            }
            "Social" -> {
                (requireActivity() as SocialSignUpActivity).changeFragments(SocialInfoFragment())
                socialViewModel.setUserAgreements(viewModel.returnUserAgreeResult())
            }
        }

    }

}