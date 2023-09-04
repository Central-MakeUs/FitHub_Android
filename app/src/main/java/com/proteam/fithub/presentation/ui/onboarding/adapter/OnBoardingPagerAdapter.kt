package com.proteam.fithub.presentation.ui.onboarding.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.proteam.fithub.presentation.ui.onboarding.inner.OnBoardingFragment

class OnBoardingPagerAdapter(activity : FragmentActivity) : FragmentStateAdapter(activity) {
    private val fragments = mutableListOf<Fragment>()
    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun setFragments() {
        fragments.apply {
            for (i in 1 until 5) {
                add(OnBoardingFragment(i))
            }
        }
    }
}