package com.proteam.fithub.presentation.ui.signup.phone

import android.content.Context
import android.os.Bundle
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
        initInputMethodManager()
        initInclude()
        observeNextEnable()

        return binding.root
    }

    private fun initBinding() {
        binding.fragment = this
    }

    private fun initInputMethodManager() {
        imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onResume() {
        super.onResume()
        binding.fgSignUpPhoneNumberEdtPhoneNumber.phoneNumberEdt().showKeyboard()
    }

    private fun initInclude() {
        binding.fgSignUpPhoneNumberEdtPhoneNumber.apply {
            getAttr(true)
            phoneNumberEdt().requestFocus()
        }
    }

    private fun observeNextEnable() {
        binding.fgSignUpPhoneNumberEdtPhoneNumber.isComplete.observe(viewLifecycleOwner) {
            if(it) {
                showTelecomField()
                binding.fgSignUpBirthdayEdtBirth.birthDayEdt().requestFocus()
            }
        }

        binding.fgSignUpBirthdayEdtBirth.isComplete.observe(viewLifecycleOwner) {
            if(it) showNameField()
        }

        binding.fgSignUpPhoneNumberEdtName.isComplete.observe(viewLifecycleOwner) {
            binding.fgSignUpPhoneNumberBtnNext.isEnabled = nameStatusCheck()
        }
    }

    private fun observeTelecom() {
        viewModel.selectTelecom.observe(viewLifecycleOwner) {
            binding.fgSignUpPhoneNumberEdtTelecom.getUserSelectedTelecom(it)
        }

        viewModel.selectTelecomState.observe(viewLifecycleOwner) {
            if (it) {
                showBirthdayField()
            }
        }
    }

    fun onNextClicked() {
        openCheckAuthCode()
    }

    private fun openCheckAuthCode() {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.sign_up_layout_container, SignUpAuthCodeFragment(), "SignUp")
            .addToBackStack("AuthCode").commit()
        hideKeyboard()
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
            visibility = View.VISIBLE
            this.birthDayEdt().showKeyboard()
        }
    }

    private fun showNameField() {
        binding.fgSignUpPhoneNumberBtnNext.text = "인증번호 전송"
        binding.fgSignUpPhoneNumberEdtName.apply {
            visibility = View.VISIBLE
            requestFocus()
        }
    }

    /** For Check Status **/

    private fun phoneStatusCheck(): Boolean =
        binding.fgSignUpPhoneNumberEdtPhoneNumber.isComplete.value == true

    private fun telecomStatusCheck(): Boolean =
        phoneStatusCheck() && binding.fgSignUpPhoneNumberEdtTelecom.isComplete.value == true

    private fun birthdayStatusCheck(): Boolean =
        telecomStatusCheck() && binding.fgSignUpBirthdayEdtBirth.isComplete.value == true

    private fun nameStatusCheck(): Boolean =
        birthdayStatusCheck() && binding.fgSignUpPhoneNumberEdtName.isComplete.value == true

    private fun EditText.showKeyboard() = imm.showSoftInput(this, 0)
    private fun hideKeyboard() {
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}