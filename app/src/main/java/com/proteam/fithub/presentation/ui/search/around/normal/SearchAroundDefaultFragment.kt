package com.proteam.fithub.presentation.ui.search.around.normal

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.chip.Chip
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSearchAroundDefaultBinding
import com.proteam.fithub.presentation.ui.search.around.AroundSearchActivity
import com.proteam.fithub.presentation.ui.search.around.normal.viewmodel.SearchAroundDefaultViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchAroundDefaultFragment : Fragment() {
    private lateinit var binding: FragmentSearchAroundDefaultBinding
    private val viewModel : SearchAroundDefaultViewModel by viewModels()
    //private val aroundViewModel : AroundSearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search_around_default,
            container,
            false
        )
        requestData()
        initUi()

        return binding.root
    }

    private fun requestData() {
        observeKeywords()
    }

    private fun observeKeywords() {
        viewModel.examKeywords.observe(viewLifecycleOwner) {
            binding.fgSearchAroundDefaultChipgroupRecommends.apply {
                for (i in it) {
                    this.addView(setChip(i))
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

            setOnClickListener { (requireActivity() as AroundSearchActivity).clickRecommends(this.tag.toString()) }
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