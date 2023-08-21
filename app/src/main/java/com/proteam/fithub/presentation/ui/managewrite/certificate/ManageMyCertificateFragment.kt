package com.proteam.fithub.presentation.ui.managewrite.certificate

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
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentMyCertificateBinding
import com.proteam.fithub.presentation.ui.detail.certificate.ExerciseCertificateDetailActivity
import com.proteam.fithub.presentation.ui.managewrite.certificate.adapter.ManageMyCertificateAdapter
import com.proteam.fithub.presentation.ui.managewrite.certificate.viewmodel.ManageMyCertificateViewModel
import com.proteam.fithub.presentation.ui.managewrite.viewmodel.ManageMyWriteViewModel
import com.proteam.fithub.presentation.ui.write.certificate.WriteOrModifyCertificateActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ManageMyCertificateFragment : Fragment() {

    private lateinit var binding : FragmentMyCertificateBinding
    private val viewModel : ManageMyCertificateViewModel by viewModels()
    private val manageViewModel : ManageMyWriteViewModel by activityViewModels()

    private val certificateAdapter by lazy {
        ManageMyCertificateAdapter(::onItemClicked, ::onCheckClicked)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_certificate, container, false)

        requestMyCertificate()
        initBinding()
        initUi()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.initAllClicked()
        certificateAdapter.refresh()
    }

    private fun requestMyCertificate() {
        manageViewModel.selectedFilter.observe(viewLifecycleOwner) {
            requestData(it)
        }
    }

    private fun initBinding() {
        binding.fragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initUi() {
        initRv()
    }

    private fun initRv() {
        binding.fgMyCertificateRvContents.adapter = certificateAdapter
        binding.fgMyCertificateRvContents.itemAnimator = null
    }

    private fun requestData(filter : Int) {
        lifecycleScope.launch {
            viewModel.requestMyCertificateData(filter).collect {
                certificateAdapter.submitData(it)
            }
        }

        certificateAdapter.addLoadStateListener {
            binding.fgMyCertificateLayoutNone.visibility = if(it.append.endOfPaginationReached && certificateAdapter.itemCount == 0) View.VISIBLE else View.GONE
        }
    }

    private fun onItemClicked(index : Int) {
        startActivity(Intent(requireActivity(), ExerciseCertificateDetailActivity::class.java).setType(index.toString()))
    }

    private fun onCheckClicked(index : Int, selected : Boolean) {
        viewModel.manageSelectItems(index, selected)
    }

    fun onAllSelectClicked() {
        viewModel.manageAllSelectedItems(certificateAdapter.checkAll(viewModel.isAllClicked.value!!))
    }

    fun onSelectDeleteClicked() {
        viewModel.requestDeleteMyCertificate()
        observeDeleteStatus()
    }

    fun onWriteCertificate() {
        startActivity(Intent(requireActivity(), WriteOrModifyCertificateActivity::class.java).setType("Write"))
    }

    private fun observeDeleteStatus() {
        viewModel.deleteStatus.observe(viewLifecycleOwner) {
            if(it == 2000) certificateAdapter.refresh()
        }
    }
}