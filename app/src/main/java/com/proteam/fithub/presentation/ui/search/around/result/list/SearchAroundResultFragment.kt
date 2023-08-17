package com.proteam.fithub.presentation.ui.search.around.result.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSearchAroundResultBinding
import com.proteam.fithub.presentation.ui.search.around.AroundSearchActivity
import com.proteam.fithub.presentation.ui.search.around.result.list.adapter.SearchAroundResultAdapter
import com.proteam.fithub.presentation.ui.search.around.viewmodel.AroundSearchViewModel

class SearchAroundResultFragment : Fragment() {
    private lateinit var binding : FragmentSearchAroundResultBinding
    private val viewModel : AroundSearchViewModel by activityViewModels()

    private val resultAdapter by lazy {
        SearchAroundResultAdapter()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_around_result, container, false)

        setResultFragment()
        initBinding()
        initUi()

        return binding.root
    }

    private fun setResultFragment() {
        viewModel.currentMarkerItems.observe(viewLifecycleOwner) {
            resultAdapter.locationData = it
            resultAdapter.notifyItemRangeChanged(0, it.size)
        }
    }

    private fun initUi() {
        binding.fgSearchAroundResultRvContents.adapter = resultAdapter
    }

    private fun initBinding() {
        binding.fragment = this
    }

    fun onOpenMapClicked() {
        (requireActivity() as AroundSearchActivity).openMapFragment()
    }
}