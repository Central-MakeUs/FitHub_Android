package com.proteam.fithub.presentation.ui.sign.up.number.info

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
import androidx.fragment.app.viewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentNumberInfoBinding
import com.proteam.fithub.presentation.component.ComponentDialogYesNo
import com.proteam.fithub.presentation.ui.sign.up.common.authcode.AuthCodeFragment
import com.proteam.fithub.presentation.ui.sign.up.number.NumberSignUpActivity
import com.proteam.fithub.presentation.ui.sign.up.number.info.dialog.NumberInfoTelecomDialog
import com.proteam.fithub.presentation.ui.sign.up.number.info.viewmodel.NumberInfoViewModel
import com.proteam.fithub.presentation.ui.sign.up.number.viewmodel.NumberSignUpViewModel
import com.proteam.fithub.presentation.util.AnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NumberInfoFragment : Fragment() {
    private lateinit var binding: FragmentNumberInfoBinding
    private val viewModel: NumberInfoViewModel by viewModels()
    private val numberViewModel : NumberSignUpViewModel by activityViewModels()

    private lateinit var imm: InputMethodManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_number_info,
            container,
            false
        )

        AnalyticsHelper.setAnalyticsLog(this.javaClass.simpleName)

        initInputMethodManager()

        initBinding()
        initUi()
        observeItemCompleted()

        return binding.root
    }

    private fun initInputMethodManager() {
        imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private fun initBinding() {
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initUi() {
        initComponent()
        initTelecom()
    }

    private fun initComponent() {
        binding.fgSignUpPhoneNumberEdtPhoneNumber.apply {
            returnEdtComponent().requestFocus()
            setErrorEnable(true)
        }
    }

    private fun initTelecom() {
        numberViewModel.initTelecom()
    }

    override fun onResume() {
        super.onResume()
        binding.fgSignUpPhoneNumberEdtPhoneNumber.returnEdtComponent().showKeyboard()
    }

    private fun observeItemCompleted() {
        binding.fgSignUpPhoneNumberEdtPhoneNumber.isFinished.observe(viewLifecycleOwner) {
            if (it && binding.fgSignUpPhoneNumberEdtTelecom.visibility == View.GONE) {
                showTelecomField()
            }
            checkNextBtnEnabled()
        }

        numberViewModel.selectTelecomState.observe(viewLifecycleOwner) {
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
        numberViewModel.selectTelecom.observe(viewLifecycleOwner) {
            binding.fgSignUpPhoneNumberEdtTelecom.getUserSelectedTelecom(it)
        }
    }

    fun onNextClicked() {
        openCheckAuthCode()
    }

    private fun openCheckAuthCode() {
        saveUserInputData()
        requestPhoneNumberAvailable()
        hideKeyboard()
    }

    private fun requestPhoneNumberAvailable() {
        viewModel.requestExistPhone(binding.fgSignUpPhoneNumberEdtPhoneNumber.returnUserInputContent())
        observePhoneNumberState()
    }

    private fun observePhoneNumberState() {
        viewModel.phoneAvailableState.observe(viewLifecycleOwner) {
            if(it == 2000) (requireActivity() as NumberSignUpActivity).changeFragments(AuthCodeFragment())
            else if(it == 4018) ComponentDialogYesNo(::moveToSignIn).show(requireActivity().supportFragmentManager, it.toString())
        }
    }

    private fun moveToSignIn() {
        requireActivity().finish()
    }

    private fun saveUserInputData() {
        numberViewModel.setUserPhoneNumber(binding.fgSignUpPhoneNumberEdtPhoneNumber.returnUserInputContent())
        numberViewModel.setUserBirth(binding.fgSignUpBirthdayEdtBirth.getUserInputBirth())
        numberViewModel.setUserGender(binding.fgSignUpBirthdayEdtBirth.getUserInputGender())
        numberViewModel.setUserName(binding.fgSignUpPhoneNumberEdtName.getUserInputContent())
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
        NumberInfoTelecomDialog().show(
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
                hideKeyboard()
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

    private fun EditText.showKeyboard()  {
        if(imm != null) {
            imm.showSoftInput(this, 0)
        } else {
            Log.d("----", "showKeyboard: IMM IS NULL")
        }
    }
    private fun hideKeyboard() {
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun numberBinding() = binding.fgSignUpPhoneNumberEdtPhoneNumber
    fun telecomBinding() = binding.fgSignUpPhoneNumberEdtTelecom
    fun birthBinding() = binding.fgSignUpBirthdayEdtBirth
    fun nameBinding() = binding.fgSignUpPhoneNumberEdtName
}