package com.proteam.fithub.presentation.ui.signup.phone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSignUpPhoneNumberAuthBinding
import com.proteam.fithub.presentation.ui.signup.interest.SignUpSelectInterestSportsFragment
import com.proteam.fithub.presentation.ui.signup.phone.dialog.SignUpPhoneNumberSelectTelecomDialog
import com.proteam.fithub.presentation.ui.signup.viewmodel.SignUpViewModel

class SignUpPhoneNumberFragment : Fragment() {
    private lateinit var binding : FragmentSignUpPhoneNumberAuthBinding
    private val viewModel : SignUpViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_phone_number_auth, container, false)

        initBinding()
        initInclude()
        observeNextEnable()

        return binding.root
    }

    private fun initBinding() {
        binding.fragment = this
    }

    private fun initInclude() {
        binding.fgSignUpPhoneNumberEdtPhoneNumber.getAttr(true)
    }

    private fun observeNextEnable() {
        binding.fgSignUpPhoneNumberEdtPhoneNumber.doneState.observe(viewLifecycleOwner) {
            binding.fgSignUpPhoneNumberBtnNext.isEnabled = checkPhoneAndTelecomToEnableNext(it)
        }

        binding.fgSignUpBirthdayEdtBirth.doneState.observe(viewLifecycleOwner) {
            binding.fgSignUpPhoneNumberBtnNext.isEnabled = it
        }
    }

    private fun observeTelecom() {
        viewModel.selectTelecom.observe(viewLifecycleOwner) {
            binding.fgSignUpPhoneNumberEdtTelecom.getUserSelectedTelecom(it)
        }
        viewModel.selectTelecomState.observe(viewLifecycleOwner) {
            if(it) initBirthCheck()
        }
    }

    fun onNextClicked() {
        if (!viewModel.selectTelecomState.value!!) {
            initTelecomClick()
            observeTelecom()
            SignUpPhoneNumberSelectTelecomDialog().show(requireActivity().supportFragmentManager, "Select_Telecom")

            binding.fgSignUpPhoneNumberBtnNext.isEnabled = false
        } else {

            /** 임시 **/
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.sign_up_layout_container, SignUpSelectInterestSportsFragment()).addToBackStack(null).commit()
        }
    }

    private fun initTelecomClick() {
        binding.fgSignUpPhoneNumberEdtTelecom.apply {
            visibility = View.VISIBLE
            setOnClickListener { SignUpPhoneNumberSelectTelecomDialog().show(requireActivity().supportFragmentManager, "Select_Telecom") }
        }
    }

    private fun initBirthCheck() {
        binding.fgSignUpBirthdayEdtBirth.apply {
            visibility = View.VISIBLE
        }
    }

    private fun checkPhoneAndTelecomToEnableNext(phoneState : Boolean) : Boolean {
        return if(binding.fgSignUpPhoneNumberEdtTelecom.visibility == View.GONE)  phoneState else phoneState && binding.fgSignUpPhoneNumberEdtTelecom.doneState.value == true
    }

}