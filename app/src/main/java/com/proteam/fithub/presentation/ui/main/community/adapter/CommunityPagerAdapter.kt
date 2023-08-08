package com.proteam.fithub.presentation.ui.main.community.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.proteam.fithub.presentation.ui.main.community.article.ArticleFragment
import com.proteam.fithub.presentation.ui.main.community.record.RecordFragment

class CommunityPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    var fragments = mutableListOf<Fragment>()

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun setFragments() {
        fragments.apply {
            add(RecordFragment("Community"))
            add(ArticleFragment())
        }
    }
}