package com.proteam.fithub.presentation.ui.search.community.none

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSearchNoneBinding

class SearchNoneFragment : Fragment() {
    private lateinit var binding : FragmentSearchNoneBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_none, container, false)

        binding.keyword = tag

        return binding.root
    }
}