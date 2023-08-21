package com.proteam.fithub.presentation.ui.sign.up.common.password

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentPasswordBinding
import com.proteam.fithub.presentation.component.ComponentAlertToast
import com.proteam.fithub.presentation.component.ComponentDialogOneButton
import com.proteam.fithub.presentation.ui.change_password.ChangePasswordActivity
import com.proteam.fithub.presentation.ui.findpassword.viewmodel.FindPasswordViewModel
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.main.mypage.MyPageFragment
import com.proteam.fithub.presentation.ui.manageinfo.ManageMyInfoActivity
import com.proteam.fithub.presentation.ui.sign.`in`.social.SocialSignInActivity
import com.proteam.fithub.presentation.ui.sign.result.SignUpResultActivity
import com.proteam.fithub.presentation.ui.sign.up.common.profile.UserProfileFragment
import com.proteam.fithub.presentation.ui.sign.up.number.NumberSignUpActivity
import com.proteam.fithub.presentation.ui.sign.up.number.viewmodel.NumberSignUpViewModel

class PasswordFragment : Fragment() {
    private lateinit var binding: FragmentPasswordBinding

    private val numberViewModel: NumberSignUpViewModel by activityViewModels()
    private val findPasswordViewModel: FindPasswordViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_password, container, false)


        initBinding()
        initInclude()
        initIncludeObserve()
        initNextBtnEnableObserve()

        return binding.root
    }

    private fun initBinding() {
        binding.fragment = this
    }

    private fun initInclude() {
        binding.fgSignUpSetPasswordEdtPassword.setErrorEnable(state = true, forCheck = false)
        binding.fgSignUpSetPasswordEdtPasswordCheck.setErrorEnable(state = true, forCheck = true)
    }

    private fun initIncludeObserve() {
        binding.fgSignUpSetPasswordEdtPassword.userInputPassword.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.fgSignUpSetPasswordEdtPasswordCheck.setPasswordForCheck(it)
                binding.fgSignUpSetPasswordEdtPasswordCheck.checkWhenOriginalChanged()
            }
        }
    }

    private fun initNextBtnEnableObserve() {
        binding.fgSignUpSetPasswordEdtPassword.isFinished.observe(viewLifecycleOwner) {
            binding.fgSignUpSetPasswordBtnNext.isEnabled =
                it && binding.fgSignUpSetPasswordEdtPasswordCheck.isFinished.value == true
        }
        binding.fgSignUpSetPasswordEdtPasswordCheck.isFinished.observe(viewLifecycleOwner) {
            binding.fgSignUpSetPasswordBtnNext.isEnabled =
                it && binding.fgSignUpSetPasswordEdtPassword.isFinished.value == true
        }
    }

    fun onNextClicked() {
        when (tag) {
            "Find" -> changePasswords()
            "Number" -> {
                setUserPassword()
                (requireActivity() as NumberSignUpActivity).changeFragments(UserProfileFragment())
            }
            "Change" -> changePasswordsOnMyPage()
        }
    }

    private fun setUserPassword() {
        numberViewModel.setUserPassword(binding.fgSignUpSetPasswordEdtPassword.returnUserInputContent())
    }

    private fun changePasswords() {
        findPasswordViewModel.requestChangePassword(binding.fgSignUpSetPasswordEdtPassword.returnUserInputContent())
        observePasswordResult()
    }

    private fun changePasswordsOnMyPage() {
        findPasswordViewModel.requestChangePasswordOnMyPage(binding.fgSignUpSetPasswordEdtPassword.returnUserInputContent())
        observePasswordResult()
    }

    private fun observePasswordResult() {
        findPasswordViewModel.passwordResult.observe(viewLifecycleOwner) {
            if (it == 2000) ComponentDialogOneButton(::showSignIn).show(
                requireActivity().supportFragmentManager,
                "RESET_PASSWORD"
            )
            else ComponentAlertToast().show(requireActivity().supportFragmentManager, it.toString())
        }
    }

    private fun showSignIn() {
        if (requireActivity().javaClass.toString().contains("Change")) {
            (requireActivity() as ChangePasswordActivity).setResult(RESULT_OK, Intent(requireActivity(), ManageMyInfoActivity::class.java).putExtra("state", true))
        }
        requireActivity().finish()
    }

    fun bindPassword() = binding.fgSignUpSetPasswordEdtPassword
    fun bindPasswordCheck() = binding.fgSignUpSetPasswordEdtPasswordCheck

}