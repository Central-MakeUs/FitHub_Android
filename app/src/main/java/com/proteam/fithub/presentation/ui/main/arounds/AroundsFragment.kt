package com.proteam.fithub.presentation.ui.main.arounds

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.databinding.FragmentAroundsBinding
import com.proteam.fithub.presentation.ui.main.arounds.list.AroundListFragment
import com.proteam.fithub.presentation.ui.main.arounds.map.AroundsMapFragment
import com.proteam.fithub.presentation.ui.main.arounds.viewmodel.AroundsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AroundsFragment : Fragment() {
    private val REQUEST_PERMISSION_LOCATION = 10

    private lateinit var binding: FragmentAroundsBinding
    private val viewModel: AroundsViewModel by activityViewModels()

    private val mapsFragment by lazy {
        AroundsMapFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_arounds, container, false)

        initUi()
        checkPermissionForLocation(requireActivity())

        return binding.root
    }

    private fun initUi() {
        initExerciseFilter()
        initMapFragment()
        observeFilterExercises()
        observeSelectedFilter()
    }


    private fun checkPermissionForLocation(context: Context): Boolean {
        return if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSION_LOCATION
            )
            false
        }
    }

    private fun observeFilterExercises() {
        viewModel.exerciseFilters.observe(viewLifecycleOwner) {
            addChips(it)
        }
    }

    private fun initExerciseFilter() {
        binding.fgAroundsChipgroupExerciseFilter.apply {
            isSingleSelection = true
            setOnCheckedStateChangeListener { _, _ ->
                if (checkedChipId == -1) return@setOnCheckedStateChangeListener
                for (i in 0 until this.childCount) {
                    this[i].isClickable = true
                }
                this[checkedChipId].isClickable = false
                viewModel.setSelectedFilter(checkedChipId)
                //Log.e("----", "initExerciseFilter: $checkedChipId", )
            }
        }
    }

    private fun addChips(items: MutableList<ResponseExercises.ExercisesList>) {
        binding.fgAroundsChipgroupExerciseFilter.apply {
            for (i in getChipList(items)) {
                this.addView(i)
            }
            this[viewModel.selectedFilter.value ?: 0].id.apply {
                check(this)
            }
        }
    }

    private fun getChipList(items: MutableList<ResponseExercises.ExercisesList>): List<Chip> {
        return mutableListOf<Chip>().apply {
            for (item in items) {
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

    private fun observeSelectedFilter() {
        viewModel.selectedFilter.observe(viewLifecycleOwner) {
            viewModel.setSearchNeed(false)
        }
    }

    private fun initMapFragment() {
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fg_arounds_layout_container, AroundsMapFragment()).commit()
    }
}