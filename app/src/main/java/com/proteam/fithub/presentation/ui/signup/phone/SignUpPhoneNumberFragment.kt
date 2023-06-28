package com.proteam.fithub.presentation.ui.signup.phone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSignUpPhoneNumberAuthBinding
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
        observeInclude()

        return binding.root
    }

    private fun initBinding() {
        binding.fragment = this
    }

    private fun initInclude() {
        binding.fgSignUpPhoneNumberEdtPhoneNumber.getAttr("phone_error")
    }

    private fun observeInclude() {
        binding.fgSignUpPhoneNumberEdtPhoneNumber.status.observe(viewLifecycleOwner) {
            binding.fgSignUpPhoneNumberBtnNext.isEnabled = it
        }
    }

    fun onNextClicked() {
        Toast.makeText(requireContext(), "성공", Toast.LENGTH_SHORT).show()
    }



}