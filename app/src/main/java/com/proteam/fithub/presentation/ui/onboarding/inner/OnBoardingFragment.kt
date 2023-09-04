package com.proteam.fithub.presentation.ui.onboarding.inner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentOnboardingBinding

class OnBoardingFragment(private val type : Int) : Fragment() {
    private lateinit var binding : FragmentOnboardingBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOnboardingBinding.inflate(inflater)

        initUi()

        return binding.root
    }

    private fun initUi() {
        when(type) {
            1 -> {
                binding.fgOnboardingLayoutBackground.setBackgroundResource(R.drawable.bg_onbording_1)
                binding.fgOnboardingTvTitle.setText(R.string.onboarding_title_1)
                binding.fgOnboardingTvSubtitle.setText(R.string.onboarding_subTitle_1)
            }

            2 -> {
                binding.fgOnboardingLayoutBackground.setBackgroundResource(R.drawable.bg_onbording_2)
                binding.fgOnboardingTvTitle.setText(R.string.onboarding_title_2)
                binding.fgOnboardingTvSubtitle.setText(R.string.onboarding_subTitle_2)
            }

            3 -> {
                binding.fgOnboardingLayoutBackground.setBackgroundResource(R.drawable.bg_onbording_3)
                binding.fgOnboardingTvTitle.setText(R.string.onboarding_title_3)
                binding.fgOnboardingTvSubtitle.setText(R.string.onboarding_subTitle_3)
            }

            4 -> {
                binding.fgOnboardingLayoutBackground.setBackgroundResource(R.drawable.bg_onbording_4)
                binding.fgOnboardingTvTitle.setText(R.string.onboarding_title_4)
                binding.fgOnboardingTvSubtitle.setText(R.string.onboarding_subTitle_4)
            }
        }
    }
}