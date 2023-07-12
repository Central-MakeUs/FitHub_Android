package com.proteam.fithub.presentation.ui.signup.phone

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSignUpPhoneNumberAuthBinding
import com.proteam.fithub.presentation.ui.signup.SignUpActivity
import com.proteam.fithub.presentation.ui.signup.authcode.SignUpAuthCodeFragment
import com.proteam.fithub.presentation.ui.signup.phone.dialog.SignUpPhoneNumberSelectTelecomDialog
import com.proteam.fithub.presentation.ui.signup.viewmodel.SignUpViewModel

class SignUpPhoneNumberFragment : Fragment() {
    private lateinit var binding: FragmentSignUpPhoneNumberAuthBinding
    private val viewModel: SignUpViewModel by activityViewModels()

    private lateinit var imm: InputMethodManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sign_up_phone_number_auth,
            container,
            false
        )

        initBinding()
        initViewModelData()
        initInputMethodManager()
        initInclude()
        observeItemCompleted()

        return binding.root
    }

    private fun initBinding() {
        binding.fragment = this
    }

    private fun initViewModelData() {
        viewModel.initUserInfo()
    }

    private fun initInputMethodManager() {
        imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onResume() {
        super.onResume()
        binding.fgSignUpPhoneNumberEdtPhoneNumber.returnEdtComponent().showKeyboard()
    }

    private fun initInclude() {
        binding.fgSignUpPhoneNumberEdtPhoneNumber.apply {
            returnEdtComponent().requestFocus()
            setErrorEnable(true)
        }
    }

    private fun observeItemCompleted() {
        binding.fgSignUpPhoneNumberEdtPhoneNumber.isFinished.observe(viewLifecycleOwner) {
            if (it && binding.fgSignUpPhoneNumberEdtTelecom.visibility == View.GONE) {
                showTelecomField()
            }
            checkNextBtnEnabled()
        }

        viewModel.selectTelecomState.observe(viewLifecycleOwner) {
            if (it && binding.fgSignUpBirthdayEdtBirth.visibility == View.GONE) {
                showBirthdayField()
            }
            checkNextBtnEnabled()
        }

        binding.fgSignUpBirthdayEdtBirth.isFinished.observe(viewLifecycleOwner) {
            if (it && binding.fgSignUpPhoneNumberEdtName.visibility == View.GONE) showNameField()
            checkNextBtnEnabled()
        }

        binding.fgSignUpPhoneNumberEdtName.isFinished.observe(viewLifecycleOwner) {
            checkNextBtnEnabled()
        }
    }

    private fun checkNextBtnEnabled() {
        binding.fgSignUpPhoneNumberBtnNext.isEnabled = nameStatusCheck()
    }

    private fun observeTelecom() {
        viewModel.selectTelecom.observe(viewLifecycleOwner) {
            binding.fgSignUpPhoneNumberEdtTelecom.getUserSelectedTelecom(it)
        }
    }

    fun onNextClicked() {
        openCheckAuthCode()
    }

    private fun openCheckAuthCode() {
        saveUserInputData()
        (requireActivity() as SignUpActivity).changeFragments(SignUpAuthCodeFragment())
        hideKeyboard()
    }

    private fun saveUserInputData() {
        viewModel.setUserPhoneNumber(binding.fgSignUpPhoneNumberEdtPhoneNumber.returnUserInputContent())
        viewModel.setUserBirth(binding.fgSignUpBirthdayEdtBirth.getUserInputContent())
        viewModel.setUserName(binding.fgSignUpPhoneNumberEdtName.getUserInputContent())
    }

    private fun initTelecomClick() {
        binding.fgSignUpPhoneNumberEdtTelecom.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                openTelecomDialog()
            }
        }
    }

    private fun showTelecomField() {
        initTelecomClick()
        observeTelecom()
        openTelecomDialog()
    }

    private fun openTelecomDialog() {
        SignUpPhoneNumberSelectTelecomDialog().show(
            requireActivity().supportFragmentManager,
            "Select_Telecom"
        )
    }

    private fun showBirthdayField() {
        binding.fgSignUpBirthdayEdtBirth.apply {
            setErrorEnable(true)
            visibility = View.VISIBLE
            this.birthDayEdt().apply {
                requestFocus()
                showKeyboard()
            }
        }
    }

    private fun showNameField() {
        binding.fgSignUpPhoneNumberEdtName.apply {
            setErrorEnable(true)
            visibility = View.VISIBLE
            requestFocus()
        }
    }

    /** For Check Status **/

    private fun phoneStatusCheck(): Boolean =
        binding.fgSignUpPhoneNumberEdtPhoneNumber.isFinished.value == true

    private fun telecomStatusCheck(): Boolean =
        phoneStatusCheck() && binding.fgSignUpPhoneNumberEdtTelecom.isComplete.value == true

    private fun birthdayStatusCheck(): Boolean =
        telecomStatusCheck() && binding.fgSignUpBirthdayEdtBirth.isFinished.value == true

    private fun nameStatusCheck(): Boolean =
        birthdayStatusCheck() && binding.fgSignUpPhoneNumberEdtName.isFinished.value == true

    private fun EditText.showKeyboard() = imm.showSoftInput(this, 0)
    private fun hideKeyboard() {
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}