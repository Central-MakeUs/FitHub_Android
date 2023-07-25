package com.proteam.fithub.presentation.ui.signup.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSignUpUserProfileBinding
import com.proteam.fithub.presentation.ui.signup.SignUpActivity
import com.proteam.fithub.presentation.ui.signup.interest.SelectInterestSportsFragment
import com.proteam.fithub.presentation.ui.signup.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpUserProfileFragment : Fragment() {
    private lateinit var binding: FragmentSignUpUserProfileBinding
    private lateinit var imm: InputMethodManager

    private val viewModel: SignUpViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sign_up_user_profile,
            container,
            false
        )

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
        binding.signUpViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initInclude() {
        viewModel.initProfile()
        Log.e("----", "initInclude: clear", )
        binding.fgSignUpUserProfileEdtNickname.setErrorEnable(true)
    }

    private fun observeInclude() {
        binding.fgSignUpUserProfileEdtNickname.isFinished.observe(viewLifecycleOwner) {
            binding.fgSignUpUserProfileBtnNext.isEnabled = it
        }

        binding.fgSignUpUserProfileEdtNickname.checkSameClicked.observe(viewLifecycleOwner) {
            viewModel.requestCheckSameNickName(binding.fgSignUpUserProfileEdtNickname.getUserInputContent())
        }

        /** 닉네임 중복여부 체크 **/

        viewModel.checkNickNameResult.observe(viewLifecycleOwner) {
            binding.fgSignUpUserProfileEdtNickname.getCheckResult(it)
        }
    }

    fun onGalleryOpen() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        this.requestGalleryActivity.launch(intent)
    }

  private val requestGalleryActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data?.data != null) {
                val clipData = it?.data?.clipData
                if (clipData == null) {
                    it.data!!.data?.let { it1 ->
                        viewModel.setUserSelectedProfile(it1)
                        //binding.fgSignUpUserProfileIvProfile.setImageURI(it1)
                    }
                }
            }
        }


    fun onNextClicked() {
        when (tag) {
            "Sign_Up" -> {
                setUserInputData()
                (requireActivity() as SignUpActivity).changeFragments(SelectInterestSportsFragment())
            }
        }
        hideKeyboard()
    }

    private fun setUserInputData() {
        viewModel.setUserNickname(binding.fgSignUpUserProfileEdtNickname.getUserInputContent())
    }

    private fun hideKeyboard() {
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}