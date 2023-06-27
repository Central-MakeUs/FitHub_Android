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
import com.proteam.fithub.presentation.ui.signup.agree.adapter.SignUpAgreeAdapter
import com.proteam.fithub.presentation.ui.signup.viewmodel.SignUpViewModel

class SignUpAgreementFragment : Fragment() {
    private lateinit var binding : FragmentSignUpAgreementBinding
    private val viewModel : SignUpViewModel by activityViewModels()

    private val adapter by lazy {
        SignUpAgreeAdapter(agreeContentList())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_agreement, container, false)

        initBinding()
        initAgreeRV()
        observeTest()

        return binding.root
    }

    private fun initBinding() {
        binding.viewModel = viewModel
    }

    private fun initAgreeRV() {
        binding.fgSignUpAgreementRvContents.adapter = adapter
    }

    private fun observeTest() {
        viewModel.signUpAllAgreements.observe(viewLifecycleOwner) {
            viewModel.manageAllAgreements(it)
        }
    }

    /** Local **/

    fun agreeContentList() : List<String> =
        listOf("(필수) 개인정보 수집 및 이용에 동의합니다.", "(필수) 이용약관에 동의합니다.", "(필수) 위치 기반 서비스 약관에 동의합니다.", "(필수) 만 14세 이상 입니다.", "(선택) 마케팅 정보 수신에 동의합니다.")

}