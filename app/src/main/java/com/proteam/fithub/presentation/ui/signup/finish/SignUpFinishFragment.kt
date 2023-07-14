package com.proteam.fithub.presentation.ui.signup.finish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentFinishSignUpBinding
import com.proteam.fithub.presentation.ui.signup.viewmodel.SignUpViewModel

class SignUpFinishFragment : Fragment() {
    private lateinit var binding : FragmentFinishSignUpBinding
    private val viewModel : SignUpViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_finish_sign_up, container, false)

        initBinding()

        return binding.root
    }

    private fun initBinding() {
        binding.viewModel = viewModel
    }
}