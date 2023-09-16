package com.proteam.fithub.presentation.ui.main.arounds.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseLocationData
import com.proteam.fithub.databinding.FragmentAroundListBinding
import com.proteam.fithub.presentation.ui.main.arounds.list.adapter.AroundsListAdapter
import com.proteam.fithub.presentation.ui.main.arounds.viewmodel.AroundsViewModel
import com.proteam.fithub.presentation.util.AnalyticsHelper

class AroundListFragment : Fragment() {
    private lateinit var binding : FragmentAroundListBinding
    private val viewModel : AroundsViewModel by activityViewModels()

    private val adapter : AroundsListAdapter by lazy {
        AroundsListAdapter(::onItemClicked)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_around_list, container, false)

        AnalyticsHelper.setAnalyticsLog(this.javaClass.simpleName)

        initBinding()
        initUi()
        initData()

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

    private fun initData() {
        observeLocationData()
    }

    private fun observeLocationData() {
        viewModel.filteredMarkerItems.observe(viewLifecycleOwner) {
            it?.let {
                adapter.setItems(it)
            }
        }
    }

    private fun initRV() {
        binding.fgAroundsRvContent.adapter = adapter
    }

    private fun onItemClicked(item : ResponseLocationData.LocationItems) {
        /*viewModel.setSelectedItem(item)
        openMapFragment() */
    }

    fun openMapFragment() {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
    }
}