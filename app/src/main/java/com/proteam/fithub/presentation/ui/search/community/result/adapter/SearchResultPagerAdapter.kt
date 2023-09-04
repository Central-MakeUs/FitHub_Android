package com.proteam.fithub.presentation.ui.search.community.result.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.proteam.fithub.presentation.ui.search.community.none.SearchNoneFragment
import com.proteam.fithub.presentation.ui.search.community.result.article.SearchResultArticleFragment
import com.proteam.fithub.presentation.ui.search.community.result.certificate.SearchResultCertificateFragment
import com.proteam.fithub.presentation.ui.search.community.result.total.SearchResultTotalFragment

class SearchResultPagerAdapter(fragmentActivity: Fragment) : FragmentStateAdapter(fragmentActivity) {
    var fragments = mutableListOf<Fragment>()

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }


    fun setFragments(code : Int, certificate : Boolean, article : Boolean) {
        fragments.apply {
            add(if(code == 2000) SearchResultTotalFragment() else SearchNoneFragment())
            add(if(!certificate) SearchResultCertificateFragment() else SearchNoneFragment())
            add(if(!article) SearchResultArticleFragment() else SearchNoneFragment())
        }
    }
}