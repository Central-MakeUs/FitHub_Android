package com.proteam.fithub.presentation.ui.main.community.certificate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.databinding.FragmentCommunityCertifiateBinding
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.main.community.certificate.adapter.CertificateAdapter
import com.proteam.fithub.presentation.ui.main.community.viewmodel.CommunityViewModel

class CertificateFragment : Fragment() {
    private lateinit var binding : FragmentCommunityCertifiateBinding
    private val viewModel : CommunityViewModel by activityViewModels()
    private val certificateAdapter by lazy {
        CertificateAdapter(::onHeartClicked, ::onCertificateClicked)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_certifiate, container, false)

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

    private fun requestData() {
        viewModel.requestCertificateItems()

        observeData()
    }

    private fun observeData() {
        viewModel.certificateItems.observe(viewLifecycleOwner) {
            certificateAdapter.items = it
            certificateAdapter.notifyItemRangeChanged(0, it.size)
        }
    }

    private fun initRV() {
        binding.fgCommunityCertificateRvCertificates.adapter = certificateAdapter
    }

    private fun onHeartClicked(index : Int) {
        Log.e("----", "onHeartClicked: $index", )
        //하트눌림 API
    }

    private fun onCertificateClicked(index : Int) {
        (requireActivity() as MainActivity).openCertificateDetailActivity(index)
        //상세화면으로 이동 API
    }

}