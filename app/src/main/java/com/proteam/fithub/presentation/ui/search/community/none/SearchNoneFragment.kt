package com.proteam.fithub.presentation.ui.search.community.none

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSearchNoneBinding
import com.proteam.fithub.presentation.ui.search.community.viewmodel.SearchViewModel

class SearchNoneFragment : Fragment() {
    private lateinit var binding : FragmentSearchNoneBinding
    private val viewModel : SearchViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_none, container, false)

        binding.keyword = viewModel.userInputKeyword.value

        return binding.root
    }
}