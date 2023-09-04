package com.proteam.fithub.presentation.ui.search.community.normal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSearchDefaultBinding
import com.proteam.fithub.presentation.ui.search.community.SearchActivity
import com.proteam.fithub.presentation.ui.search.community.normal.viewmodel.SearchDefaultViewModel
import com.proteam.fithub.presentation.ui.search.community.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchDefaultFragment : Fragment() {
    private lateinit var binding: FragmentSearchDefaultBinding
    private val viewModel: SearchDefaultViewModel by viewModels()
    private val searchViewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search_default, container, false)

        requestData()
        initBinding()
        initUi()

        return binding.root
    }

    private fun initBinding() {

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

    private fun initUi() {

    }

    private fun setChip(title: String): Chip {
        return Chip(requireContext()).apply {
            text = title
            tag = title
            setChipStyles()

            setOnClickListener { (requireActivity() as SearchActivity).clickRecommends(this.tag.toString()) }
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