package com.proteam.fithub.presentation.ui.search.arounds.normal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSearchAroundDefaultBinding
import com.proteam.fithub.presentation.ui.search.arounds.SearchAroundsActivity
import com.proteam.fithub.presentation.ui.search.arounds.normal.viewmodel.SearchAroundDefaultViewModel
import com.proteam.fithub.presentation.ui.search.community.SearchActivity
import com.proteam.fithub.presentation.util.AnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchAroundDefaultFragment : Fragment() {
    private lateinit var binding : FragmentSearchAroundDefaultBinding
    private val viewModel : SearchAroundDefaultViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_around_default, container, false)

        AnalyticsHelper.setAnalyticsLog(this.javaClass.simpleName)

        requestData()

        return binding.root
    }

    private fun requestData() {
        viewModel.examKeywords.observe(viewLifecycleOwner) {
            binding.fgSearchDefaultChipgroupRecommends.apply {
                for (i in it) {
                    addView(setChip(i))
                }
            }
        }
    }

    private fun setChip(title: String): Chip {
        return Chip(requireContext()).apply {
            text = title
            tag = title
            setChipStyles()

            setOnClickListener { (requireActivity() as SearchAroundsActivity).clickRecommends(this.tag.toString()) }
        }
    }

    private fun Chip.setChipStyles() {
        this.apply {
            setTextAppearance(R.style.Certificate_Chip_Text_Style)
            setChipBackgroundColorResource(R.color.bg_default)
            setChipStrokeColorResource(R.color.icon_disabled)
            chipStrokeWidth = 0.5F
        }
    }

}