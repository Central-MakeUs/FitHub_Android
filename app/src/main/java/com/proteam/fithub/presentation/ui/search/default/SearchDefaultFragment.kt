package com.proteam.fithub.presentation.ui.search.default

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSearchDefaultBinding
import com.proteam.fithub.presentation.ui.search.SearchActivity
import com.proteam.fithub.presentation.ui.search.viewmodel.SearchViewModel

class SearchDefaultFragment : Fragment() {
    private lateinit var binding : FragmentSearchDefaultBinding
    private val viewModel : SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_default, container, false)

        initBinding()
        initUi()

        return binding.root
    }

    private fun initBinding() {

    }

    private fun initUi() {
        binding.fgSearchDefaultChipgroupRecommends.apply {
            for (i in returnChipCommends()) {
                addView(setChip(i))
            }
        }
    }

    private fun setChip(title : String) : Chip {
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


    /** Dummy **/
    private fun returnChipCommends() : List<String> = listOf("클라이밍", "오운완", "프로팀", "최고짱짱", "폴댄스", "스케이트", "테니스")
}