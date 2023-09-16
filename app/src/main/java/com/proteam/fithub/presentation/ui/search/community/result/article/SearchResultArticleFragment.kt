package com.proteam.fithub.presentation.ui.search.community.result.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSearchArticleBinding
import com.proteam.fithub.presentation.ui.search.community.SearchActivity
import com.proteam.fithub.presentation.ui.search.community.result.article.adapter.SearchResultArticleAdapter
import com.proteam.fithub.presentation.ui.search.community.result.article.viewmodel.SearchResultArticleViewModel
import com.proteam.fithub.presentation.ui.search.community.viewmodel.SearchViewModel
import com.proteam.fithub.presentation.util.AnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchResultArticleFragment : Fragment() {
    private lateinit var binding : FragmentSearchArticleBinding
    private val viewModel : SearchResultArticleViewModel by viewModels()
    private val searchViewModel : SearchViewModel by activityViewModels()

    private val searchResultArticleAdapter by lazy {
        SearchResultArticleAdapter(::onHeartClicked ,::onBoardClicked)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_article, container, false)

        AnalyticsHelper.setAnalyticsLog(this.javaClass.simpleName)

        initBinding()
        initUi()
        requestData()

        return binding.root
    }

    private fun initBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initUi() {
        initRV()

    }

    override fun onResume() {
        super.onResume()
        searchResultArticleAdapter.refresh()
    }

    private fun requestData() {
        viewModel.isArticleRecentSort.observe(viewLifecycleOwner) {
            observeData(if(viewModel.isArticleRecentSort.value == true) "date" else "like", searchViewModel.userInputKeyword.value!!)
        }
    }

    private fun observeData(type : String, tag : String) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.requestSearchCertificateResult(type, tag).apply {
                collect {
                    searchResultArticleAdapter.submitData(it)
                }
            }
        }
    }
    private fun initRV() {
        binding.fgCommunityBoardRvBoard.adapter = searchResultArticleAdapter
        binding.fgCommunityBoardRvBoard.itemAnimator = null
    }

    private fun onBoardClicked(position : Int) {
        (requireActivity() as SearchActivity).openArticleDetailActivity(position)
    }

    private fun onHeartClicked(position : Int) {

    }

}