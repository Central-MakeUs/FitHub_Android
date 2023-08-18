package com.proteam.fithub.presentation.ui.main.community.record

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import com.google.android.material.snackbar.Snackbar
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentRecordBinding
import com.proteam.fithub.presentation.LoadingDialog
import com.proteam.fithub.presentation.ui.detail.certificate.ExerciseCertificateDetailActivity
import com.proteam.fithub.presentation.ui.main.community.record.adapter.RecordAdapter
import com.proteam.fithub.presentation.ui.main.community.record.viewmodel.RecordViewModel
import com.proteam.fithub.presentation.ui.main.community.viewmodel.CommunityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecordFragment(private val type: String) : Fragment() {
    private lateinit var binding: FragmentRecordBinding

    private val viewModel: RecordViewModel by viewModels()
    private val communityViewModel: CommunityViewModel by activityViewModels()

    private val recordAdapter by lazy {
        RecordAdapter(::onRecordItemClicked)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record, container, false)

        initType()
        initBinding()
        initUi()

        return binding.root
    }

    private fun initType() {
        when (type) {
            "Community" -> observeWhenCommunity()
            "Search" -> requestWhenSearch()
        }
    }

    private fun initBinding() {
        binding.fragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initUi() {
        binding.fgCommunityCertificateRvCertificates.adapter = recordAdapter
    }

    override fun onResume() {
        super.onResume()
        requestWhenCommunity(communityViewModel.selectedFilter.value ?: 0)
    }

    private fun observeWhenCommunity() {
        communityViewModel.selectedFilter.observe(viewLifecycleOwner) {
            requestWhenCommunity(it)
        }
        viewModel.recordSortType.observe(viewLifecycleOwner) {
            communityViewModel.selectedFilter.value?.let { requestWhenCommunity(it) }
        }
    }

    private fun requestWhenCommunity(filter: Int) {
        lifecycleScope.launch {
            viewModel.requestWhenCommunity(filter).collectLatest {
                recordAdapter.submitData(it)
            }
        }
    }

    private fun requestWhenSearch() {

    }

    private fun onRecordItemClicked(index: Int) {
        startActivity(Intent(requireContext(), ExerciseCertificateDetailActivity()::class.java).setType(index.toString()))
    }
}