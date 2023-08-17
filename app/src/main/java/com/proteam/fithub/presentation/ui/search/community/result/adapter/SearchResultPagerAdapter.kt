package com.proteam.fithub.presentation.ui.search.community.result.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.proteam.fithub.presentation.ui.search.community.result.article.SearchResultArticleFragment
import com.proteam.fithub.presentation.ui.search.community.result.certificate.SearchResultCertificateFragment
import com.proteam.fithub.presentation.ui.search.community.result.total.SearchResultTotalFragment

class SearchResultPagerAdapter(fragmentActivity: Fragment) : FragmentStateAdapter(fragmentActivity) {
    var fragments = mutableListOf<Fragment>()

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }


    fun setFragments() {
        fragments.apply {
            add(SearchResultTotalFragment())
            add(SearchResultCertificateFragment())
            add(SearchResultArticleFragment())
        }
    }
}