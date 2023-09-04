package com.proteam.fithub.presentation.ui.change_password.check_password

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentCheckPasswordBinding
import com.proteam.fithub.presentation.ui.change_password.check_password.viewmodel.CheckPasswordViewModel
import com.proteam.fithub.presentation.ui.sign.up.common.password.PasswordFragment
import com.proteam.fithub.presentation.util.CustomSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckPasswordFragment : Fragment() {
    private lateinit var binding: FragmentCheckPasswordBinding
    private val viewModel : CheckPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_check_password, container, false)

        initComponent()
        initBinding()

        return binding.root
    }

    private fun initComponent() {
        binding.fgCheckPasswordComponentPassword.setErrorEnable(false, false)

        binding.fgCheckPasswordComponentPassword.userInputPassword.observe(viewLifecycleOwner) {
            binding.fgCheckPasswordBtnCheck.isEnabled = it.isNotEmpty()
        }
    }

    private fun initBinding() {
        binding.fragment = this
    }

    fun onCheckClicked() {
        viewModel.requestCheckExistPassword(binding.fgCheckPasswordComponentPassword.returnUserInputContent())
        observeCheckState()
    }

    private fun observeCheckState() {
        viewModel.passwordState.observe(viewLifecycleOwner) {
            when(it) {
                0 -> return@observe
                2022 -> requireActivity().supportFragmentManager.beginTransaction().replace(R.id.change_password_layout_container, PasswordFragment(), "Change").commit()
                2023 -> CustomSnackBar.makeSnackBar(binding.root, "2023").show()
            }
            viewModel.initState()
        }
    }
}