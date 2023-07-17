package com.proteam.fithub.presentation.ui.main.community.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.proteam.fithub.presentation.ui.main.community.board.BoardFragment
import com.proteam.fithub.presentation.ui.main.community.certificate.CertificateFragment

class CommunityPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private var fragments = mutableListOf<Fragment>()

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun setFragments() {
        fragments.apply {
            add(CertificateFragment())
            add(BoardFragment())
        }
    }
}