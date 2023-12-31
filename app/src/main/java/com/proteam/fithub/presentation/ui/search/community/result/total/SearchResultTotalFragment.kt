package com.proteam.fithub.presentation.ui.search.community.result.total

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSearchResultTotalBinding
import com.proteam.fithub.presentation.ui.search.community.SearchActivity
import com.proteam.fithub.presentation.ui.search.community.result.total.adapter.ArticleAdapter
import com.proteam.fithub.presentation.ui.search.community.result.total.adapter.CertificateAdapter
import com.proteam.fithub.presentation.ui.search.community.viewmodel.SearchViewModel
import com.proteam.fithub.presentation.util.AnalyticsHelper

class SearchResultTotalFragment : Fragment() {
    private lateinit var binding: FragmentSearchResultTotalBinding
    private val viewModel: SearchViewModel by activityViewModels()

    private val certificateAdapter by lazy {
        CertificateAdapter(::onCertificateClicked)
    }
    private val articleAdapter by lazy {
        ArticleAdapter(::onArticleClicked)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search_result_total,
            container,
            false
        )

        AnalyticsHelper.setAnalyticsLog(this.javaClass.simpleName)

        initBinding()
        initUi()

        return binding.root
    }

    private fun initBinding() {
        binding.fragment = this
    }

    private fun initUi() {
        initCertificate()
        initAdapterData()
    }

    private fun initCertificate() {
        binding.itemSearchResultTotalRvCertificate.adapter = certificateAdapter
        binding.itemSearchResultTotalRvArticle.adapter = articleAdapter
    }

    private fun initAdapterData() {
        viewModel.totalSearchData.observe(viewLifecycleOwner) {
            binding.itemSearchResultTotalLayoutCertificate.visibility =
                if (it.recordPreview.recordList.isEmpty()) View.GONE else View.VISIBLE
            certificateAdapter.certificates = it.recordPreview.recordList.toMutableList()

            binding.itemSearchResultTotalLayoutArticle.visibility =
                if (it.articlePreview.articleList.isEmpty()) View.GONE else View.VISIBLE
            articleAdapter.articles = it.articlePreview.articleList.toMutableList()

            certificateAdapter.notifyItemRangeChanged(0, it.recordPreview.recordList.size)
            articleAdapter.notifyItemRangeChanged(0, it.articlePreview.articleList.size)
        }
    }

    private fun onCertificateClicked(index: Int) {
        (requireActivity() as SearchActivity).openCertificateDetailActivity(index)
    }

    private fun onArticleClicked(index: Int) {
        (requireActivity() as SearchActivity).openArticleDetailActivity(index)
    }

    fun onCertificateDetailClicked() {
        (requireActivity() as SearchActivity).changeTab(1)
    }

    fun onArticleDetailClicked() {
        (requireActivity() as SearchActivity).changeTab(2)
    }
}