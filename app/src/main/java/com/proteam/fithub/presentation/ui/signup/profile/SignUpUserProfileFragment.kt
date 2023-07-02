package com.proteam.fithub.presentation.ui.signup.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSignUpUserProfileBinding
import com.proteam.fithub.presentation.ui.signup.authcode.SignUpAuthCodeFragment
import com.proteam.fithub.presentation.ui.signup.interest.SignUpSelectInterestSportsFragment

class SignUpUserProfileFragment : Fragment() {
    private lateinit var binding : FragmentSignUpUserProfileBinding
    private lateinit var imm: InputMethodManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_user_profile, container, false)

        initBinding()
        initInclude()
        initInputMethodManager()
        observeInclude()


        return binding.root
    }

    private fun initInputMethodManager() {
        imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private fun initBinding() {
        binding.fragment = this
    }

    private fun initInclude() {
        binding.fgSignUpUserProfileEdtNickname.getAttr(true)
    }

    private fun observeInclude() {
        binding.fgSignUpUserProfileEdtNickname.doneState.observe(viewLifecycleOwner) {
            binding.fgSignUpUserProfileBtnNext.isEnabled = it
        }
    }
    fun onNextClicked() {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.sign_up_layout_container, SignUpSelectInterestSportsFragment())
            .addToBackStack("Sports").commit()
        hideKeyboard()
    }

    private fun hideKeyboard() {
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}