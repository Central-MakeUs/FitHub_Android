package com.proteam.fithub.presentation.ui.signup.phone

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.res.TypedArrayUtils.getAttr
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSignUpPhoneNumberAuthBinding
import com.proteam.fithub.presentation.ui.signup.authcode.SignUpAuthCodeFragment
import com.proteam.fithub.presentation.ui.signup.interest.SignUpSelectInterestSportsFragment
import com.proteam.fithub.presentation.ui.signup.phone.dialog.SignUpPhoneNumberSelectTelecomDialog
import com.proteam.fithub.presentation.ui.signup.viewmodel.SignUpViewModel
import kotlinx.coroutines.delay

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
        binding.fgSignUpPhoneNumberEdtPhoneNumber.doneState.observe(viewLifecycleOwner) {
            /** 생년월일이 나오기 전에만 버튼에 영향을 줘야 함 **/
            if (binding.fgSignUpBirthdayEdtBirth.visibility == View.GONE) {
                binding.fgSignUpPhoneNumberBtnNext.isEnabled = checkPhoneAndTelecomToEnableNext(it)
            }
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
            if (it) showBirthdayField()
        }
    }

    fun onNextClicked() {
        if (nameStatusCheck()) { //이름까지 입력 완료
            openCheckAuthCode()
        } else if (birthdayStatusCheck()) { //생일까지 입력 완료
            showNameField()
        } else if (telecomStatusCheck()) {
            showBirthdayField()
        } else if (phoneStatusCheck()) {
            showTelecomField()
        }
    }

    private fun openCheckAuthCode() {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.sign_up_layout_container, SignUpAuthCodeFragment())
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
        hideKeyboard()
        SignUpPhoneNumberSelectTelecomDialog().show(
            requireActivity().supportFragmentManager,
            "Select_Telecom"
        )
    }

    private fun showBirthdayField() {
        binding.fgSignUpBirthdayEdtBirth.apply {
            visibility = View.VISIBLE
            birthDayEdt().requestFocus()
        }
        binding.fgSignUpBirthdayEdtBirth.birthDayEdt().showKeyboard()
    }

    private fun showNameField() {
        binding.fgSignUpPhoneNumberBtnNext.text = "인증번호 전송"
        binding.fgSignUpPhoneNumberEdtName.apply {
            visibility = View.VISIBLE
            requestFocus()
        }
    }

    private fun checkPhoneAndTelecomToEnableNext(phoneState: Boolean): Boolean {
        return if (binding.fgSignUpPhoneNumberEdtTelecom.visibility == View.GONE) phoneState else phoneState && binding.fgSignUpPhoneNumberEdtTelecom.doneState.value == true
    }

    /** For Check Status **/

    private fun phoneStatusCheck(): Boolean =
        binding.fgSignUpPhoneNumberEdtPhoneNumber.doneState.value == true

    private fun telecomStatusCheck(): Boolean =
        phoneStatusCheck() && binding.fgSignUpPhoneNumberEdtTelecom.doneState.value == true

    private fun birthdayStatusCheck(): Boolean =
        telecomStatusCheck() && binding.fgSignUpBirthdayEdtBirth.doneState.value == true

    private fun nameStatusCheck(): Boolean =
        birthdayStatusCheck() && binding.fgSignUpPhoneNumberEdtName.doneState.value == true

    private fun EditText.showKeyboard() = imm.showSoftInput(this, 0)
    private fun hideKeyboard() {
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}