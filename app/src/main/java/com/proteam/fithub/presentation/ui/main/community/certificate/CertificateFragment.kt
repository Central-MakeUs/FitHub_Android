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
        observeFilterExercises()

        return binding.root
    }

    private fun initBinding() {
        binding.fragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initUi() {
        initChipGroup()
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

    private fun initChipGroup() {
        binding.fgCommunityCertificateChipgroupExerciseFilter.apply {
            isSingleSelection = true

            setOnCheckedStateChangeListener { _, _ ->
                if(checkedChipId == -1) return@setOnCheckedStateChangeListener
                for(i in 0 until this.childCount) {
                    this[i].isClickable = true
                }
                this[checkedChipId].isClickable = false
                requestAPI(checkedChipId)
            }
        }
    }

    private fun observeFilterExercises() {
        viewModel.exerciseFilters.observe(viewLifecycleOwner) {
            addChips(it)
        }
    }

    private fun addChips(items : MutableList<ResponseExercises.ExercisesList>) {
        binding.fgCommunityCertificateChipgroupExerciseFilter.apply {
            for(i in getChipList(items)) {
                this.addView(i)
            }
            this[0].id.apply {
                check(this)
                requestAPI(this)
            }
        }
    }

    private fun requestAPI(checkedId : Int) {
        Log.d("----", "initChipGroup: ${checkedId}")
    }

    private fun getChipList(items : MutableList<ResponseExercises.ExercisesList>) : List<Chip> {
        return mutableListOf<Chip>().apply {
            for(item in items) {
                add(Chip(requireContext()).apply {
                    id = item.id
                    text = item.name
                    setChipStyles()
                })
            }
            add(0, Chip(requireContext()).apply {
                id = 0
                text = "전체"
                setChipStyles()
            })
        }
    }

    private fun Chip.setChipStyles() {
        this.apply {
            setTextAppearance(R.style.Certificate_Chip_Text_Style)
            setChipBackgroundColorResource(R.color.selector_bg_chip_selected)
            setChipStrokeColorResource(R.color.selector_stroke_chip_selected)
            chipStrokeWidth = 0.5F
            isCheckable = true
            isCheckedIconVisible = false
        }
    }
}