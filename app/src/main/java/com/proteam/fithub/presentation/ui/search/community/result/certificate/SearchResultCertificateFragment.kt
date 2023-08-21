package com.proteam.fithub.presentation.ui.search.community.result.certificate

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
import com.proteam.fithub.databinding.FragmentSearchCertifiateBinding
import com.proteam.fithub.presentation.ui.search.community.SearchActivity
import com.proteam.fithub.presentation.ui.search.community.result.certificate.adapter.SearchResultCertificateAdapter
import com.proteam.fithub.presentation.ui.search.community.result.certificate.viewmodel.SearchResultCertificateViewModel
import com.proteam.fithub.presentation.ui.search.community.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchResultCertificateFragment : Fragment() {
    private lateinit var binding : FragmentSearchCertifiateBinding

    private val viewModel : SearchResultCertificateViewModel by viewModels()
    private val searchViewModel : SearchViewModel by activityViewModels()

    private val certificateAdapter by lazy {
        SearchResultCertificateAdapter(::onHeartClicked, ::onItemClicked)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_certifiate, container,  false)

        initBinding()
        initUi()
        requestData()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        certificateAdapter.refresh()
    }

    private fun initUi() {
        initCertificate()
    }

    private fun initCertificate() {
        binding.fgCommunityCertificateRvCertificates.adapter = certificateAdapter
    }

    private fun initBinding() {
        binding.fragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun requestData() {
        viewModel.isCertificateRecentSort.observe(viewLifecycleOwner) {
            observeData(if(it == true)"date" else "like", searchViewModel.userInputKeyword.value!!) }
        }

    private fun observeData(type : String, tag : String) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.requestSearchCertificateResult(type, tag)
                .apply {
                    collect {
                        certificateAdapter.submitData(it)
                    }
                }
        }
    }

    private fun onHeartClicked(index : Int) {

    }

    private fun onItemClicked(index : Int) {
        (requireActivity() as SearchActivity).openCertificateDetailActivity(index)
    }
}