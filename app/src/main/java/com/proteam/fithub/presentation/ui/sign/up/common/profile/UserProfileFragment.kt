package com.proteam.fithub.presentation.ui.sign.up.common.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentUserProfileBinding
import com.proteam.fithub.presentation.ui.sign.up.common.exercise.InterestExerciseFragment
import com.proteam.fithub.presentation.ui.sign.up.common.profile.viewmodel.ProfileViewModel
import com.proteam.fithub.presentation.ui.sign.up.social.SocialSignUpActivity
import com.proteam.fithub.presentation.ui.sign.up.social.viewmodel.SocialSignUpViewModel
import com.proteam.fithub.presentation.ui.signup.SignUpActivity
import com.proteam.fithub.presentation.ui.signup.interest.SelectInterestSportsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileBinding

    private val viewModel: ProfileViewModel by viewModels()
    private val socialViewModel : SocialSignUpViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false)

        initBinding()
        initUi()

        return binding.root
    }

    private fun initBinding() {
        binding.fragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initUi() {
        initComponent()
    }

    private fun initComponent() {
        binding.fgSignUpUserProfileEdtNickname.setErrorEnable(true)

        observeComponent()
    }

    private fun observeComponent() {
        binding.fgSignUpUserProfileEdtNickname.checkSameClicked.observe(viewLifecycleOwner) {
            viewModel.requestCheckSameNickName(binding.fgSignUpUserProfileEdtNickname.getUserInputContent())
        }

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
                if (it?.data?.clipData == null) {
                    it.data!!.data?.let { it1 ->
                        viewModel.setUserSelectedProfile(it1)
                    }
                }
            }
        }

    fun onNextClicked() {
        when (tag) {
            "Social" -> {
                socialViewModel.apply {
                    setUserNickName(binding.fgSignUpUserProfileEdtNickname.getUserInputContent())
                    setUserProfileImage(viewModel.userSelectedProfileImage.value.toString())
                }
                (requireActivity() as SocialSignUpActivity).changeFragments(InterestExerciseFragment())
            }
            "Sign_Up" -> { (requireActivity() as SignUpActivity).changeFragments(SelectInterestSportsFragment()) }
        }
    }

    fun nicknameBinding() = binding.fgSignUpUserProfileEdtNickname
}

    /*private fun initInputMethodManager() {
        imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }*/

    /* private fun hideKeyboard() {
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    } */