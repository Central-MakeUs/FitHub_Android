package com.proteam.fithub.presentation.ui.findpassword.phone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentFindPasswordAuthPhoneNumberBinding
import com.proteam.fithub.presentation.component.ComponentDialogYesNo
import com.proteam.fithub.presentation.ui.findpassword.FindPasswordActivity
import com.proteam.fithub.presentation.ui.findpassword.viewmodel.FindPasswordViewModel
import com.proteam.fithub.presentation.ui.sign.up.common.authcode.AuthCodeFragment
import com.proteam.fithub.presentation.ui.sign.up.number.NumberSignUpActivity
import com.proteam.fithub.presentation.util.AnalyticsHelper

class FindPasswordAuthPhoneNumberFragment : Fragment() {
    private lateinit var binding : FragmentFindPasswordAuthPhoneNumberBinding
    private val viewModel : FindPasswordViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_password_auth_phone_number, container, false)

        AnalyticsHelper.setAnalyticsLog(this.javaClass.simpleName)

        initBinding()
        initInclude()
        observeInclude()

        return binding.root
    }

    private fun initBinding() {
        binding.fragment = this
    }

    private fun initInclude() {
        binding.fgFindPasswordAuthPhoneNumberEdtPhoneNumber.setErrorEnable(true)
    }

    private fun observeInclude() {
        binding.fgFindPasswordAuthPhoneNumberEdtPhoneNumber.isFinished.observe(viewLifecycleOwner) {
            binding.fgFindPasswordAuthPhoneNumberBtnSendAuthCode.isEnabled = it
        }
    }

    fun onSendAuthClicked() {
        setUserNumber()
        viewModel.requestUserNumberAvailable()
        observePhoneNumberAvailable()
    }

    private fun observePhoneNumberAvailable() {
        viewModel.userPhoneNumberAvailable.observe(viewLifecycleOwner) {
            if(it) (requireActivity() as FindPasswordActivity).changeFragments(AuthCodeFragment()) else showDialogWhenNoAccount()
        }
    }

    private fun setUserNumber() {
        viewModel.setUserPhoneNumber(binding.fgFindPasswordAuthPhoneNumberEdtPhoneNumber.returnUserInputContent())
    }

    private fun showDialogWhenNoAccount() {
        ComponentDialogYesNo(::onDialogActionClicked).show(requireActivity().supportFragmentManager, "NO_ACCOUNT_INFO")
    }

    private fun onDialogActionClicked() {
        startActivity(Intent(requireActivity(), NumberSignUpActivity::class.java).setType("Number"))
        requireActivity().finish()
    }
}