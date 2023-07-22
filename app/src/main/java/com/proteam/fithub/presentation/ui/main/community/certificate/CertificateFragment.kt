package com.proteam.fithub.presentation.ui.main.community.certificate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentCommunityCertifiateBinding
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.main.community.certificate.adapter.CertificateAdapter
import com.proteam.fithub.presentation.ui.main.community.viewmodel.CommunityViewModel
import kotlinx.coroutines.launch

class CertificateFragment : Fragment() {
    private lateinit var binding: FragmentCommunityCertifiateBinding
    private val viewModel: CommunityViewModel by activityViewModels()

    private val certificateAdapter by lazy {
        CertificateAdapter(::onHeartClicked, ::onCertificateClicked)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_community_certifiate,
            container,
            false
        )

        initBinding()
        initUi()
        requestData()

        return binding.root
    }

    private fun initBinding() {
        binding.fragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initUi() {
        initRV()
    }

    override fun onResume() {
        super.onResume()
        observeData(if(viewModel.isCertificateRecentSort.value == true)"date" else "like", viewModel.selectedFilter.value ?: 0, false)
    }

    private fun requestData() {
        viewModel.selectedFilter.observe(viewLifecycleOwner) {
            observeData(if(viewModel.isCertificateRecentSort.value == true)"date" else "like", it, true)
        }
        viewModel.isCertificateRecentSort.observe(viewLifecycleOwner) {
            viewModel.selectedFilter.value?.let { it1 -> observeData(if(it == true)"date" else "like", it1, true) }
        }

        certificateAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    private fun observeData(type : String, category : Int, byTag : Boolean) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.requestCertificateItems(type, category).apply {
                collect {
                    certificateAdapter.submitData(it)
                }
            }
        }
    }

    private fun initRV() {
        binding.fgCommunityCertificateRvCertificates.adapter = certificateAdapter
        binding.fgCommunityCertificateRvCertificates.itemAnimator = null
    }

    private fun onHeartClicked(index: Int) {
        viewModel.requestHeartClicked(index)
    }

    private fun onCertificateClicked(index: Int) {
        (requireActivity() as MainActivity).openCertificateDetailActivity(index)
    }

}