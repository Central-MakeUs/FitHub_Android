package com.proteam.fithub.presentation.ui.search.result

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSearchResultBinding
import com.proteam.fithub.presentation.ui.search.result.adapter.SearchResultPagerAdapter

class SearchResultFragment : Fragment() {
    private lateinit var binding : FragmentSearchResultBinding

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
        initTab()
    }

    private fun initAdapter() {
        resultPagerAdapter = SearchResultPagerAdapter(this).also { it.setFragments() }
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