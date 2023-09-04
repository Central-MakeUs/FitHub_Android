package com.proteam.fithub.presentation.ui.search.community.result

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSearchResultBinding
import com.proteam.fithub.presentation.ui.search.community.none.SearchNoneFragment
import com.proteam.fithub.presentation.ui.search.community.result.adapter.SearchResultPagerAdapter
import com.proteam.fithub.presentation.ui.search.community.result.article.SearchResultArticleFragment
import com.proteam.fithub.presentation.ui.search.community.result.certificate.SearchResultCertificateFragment
import com.proteam.fithub.presentation.ui.search.community.result.total.SearchResultTotalFragment
import com.proteam.fithub.presentation.ui.search.community.viewmodel.SearchViewModel

class SearchResultFragment : Fragment() {
    private lateinit var binding : FragmentSearchResultBinding
    private val viewModel : SearchViewModel by activityViewModels()
    private var resultPagerAdapter : SearchResultPagerAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_result, container ,false)

        initBinding()

        return binding.root
    }

    private fun initBinding() {
        initAdapter()
    }

    private fun initAdapter() {
        resultPagerAdapter = SearchResultPagerAdapter(this).also { it.setFragments(viewModel.searchCode.value!!, viewModel.totalSearchData.value?.recordPreview?.recordList.isNullOrEmpty(), viewModel.totalSearchData.value?.articlePreview?.articleList.isNullOrEmpty()) }
    }

    override fun onResume() {
        super.onResume()
        initTab()
    }

    private fun initTab() {
        binding.fgSearchResultVpContainer.apply {
            adapter = resultPagerAdapter
            this.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            TabLayoutMediator(binding.fgSearchResultTabTab, this) { mTab, position ->
                mTab.text = setTabTitles()[position]
            }.attach()
            offscreenPageLimit = 1
        }
    }

    fun changeCurrentTab(position : Int) {
        binding.fgSearchResultVpContainer.setCurrentItem(position, false)
    }

    /** Dummy **/
    private fun setTabTitles() : List<String> = listOf("전체", "운동 인증", "핏사이트")
}