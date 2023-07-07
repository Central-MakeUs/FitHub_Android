package com.proteam.fithub.presentation.ui.findpassword.phone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentFindPasswordAuthPhoneNumberBinding
import com.proteam.fithub.presentation.component.ComponentDialogYesNo
import com.proteam.fithub.presentation.ui.signup.SignUpActivity
import kotlin.random.Random

class FindPasswordAuthPhoneNumberFragment : Fragment() {
    private lateinit var binding : FragmentFindPasswordAuthPhoneNumberBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_password_auth_phone_number, container, false)

        initBinding()
        initInclude()
        observeInclude()

        return binding.root
    }

    private fun initBinding() {
        binding.fragment = this
    }

    private fun initInclude() {
        binding.fgFindPasswordAuthPhoneNumberEdtPhoneNumber.getAttr(true)
    }

    private fun observeInclude() {
        binding.fgFindPasswordAuthPhoneNumberEdtPhoneNumber.isComplete.observe(viewLifecycleOwner) {
            binding.fgFindPasswordAuthPhoneNumberBtnSendAuthCode.isEnabled = it
        }
    }

    fun onSendAuthClicked() {
        //:TODO 랜덤으로 다이얼로그 / 성공 구분해둠 수정해야함!!
        val rand = Random.nextInt(0,2)
        Log.d("----", "onSendAuthClicked: $rand")
        if(rand == 0) {
            Toast.makeText(requireContext(), "있음", Toast.LENGTH_SHORT).show()
        } else {
            showDialogWhenNoAccount()
        }

    }

    private fun showDialogWhenNoAccount() {
        ComponentDialogYesNo(::onDialogActionClicked).show(requireActivity().supportFragmentManager, "NO_ACCOUNT_INFO")
    }

    private fun onDialogActionClicked() {
        startActivity(Intent(requireActivity(), SignUpActivity::class.java))
        requireActivity().finish()
    }

}