package com.proteam.fithub.presentation.ui.onboarding

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayoutMediator
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivityOnboardingBinding
import com.proteam.fithub.presentation.ui.onboarding.adapter.OnBoardingPagerAdapter
import com.proteam.fithub.presentation.ui.onboarding.viewmodel.OnBoardingViewModel
import com.proteam.fithub.presentation.ui.sign.`in`.social.SocialSignInActivity
import com.proteam.fithub.presentation.util.AnalyticsHelper

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOnboardingBinding
    private val viewModel : OnBoardingViewModel by viewModels()
    private val onBoardingAdapter by lazy {
        OnBoardingPagerAdapter(this).also { it.setFragments() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding)

        AnalyticsHelper.setAnalyticsLog(this.javaClass.simpleName)

        initBinding()
        initContentPager()
        requestPermission()
    }

    private fun requestPermission() : Boolean{
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                true
            } else { ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES), 101)
                false
            }
        }
        else {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                true
            } else { ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 101)
                false
            }
        }
    }



    private fun initBinding() {
        binding.activity = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun initContentPager() {
        binding.onboardingVpContents.apply {
            adapter = onBoardingAdapter
            TabLayoutMediator(binding.onboardingTabPagerIndicator, this) {tab, _ ->
            }.attach()

            registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.setPagerPosition(position)
                }
            })
        }
    }

    fun onNextClicked() {
        binding.onboardingVpContents.setCurrentItem(binding.onboardingVpContents.currentItem + 1, true)
    }

    fun onSkipClicked() {
        startActivity(Intent(this, SocialSignInActivity::class.java))
        finish()
    }
}