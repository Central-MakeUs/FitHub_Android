package com.proteam.fithub.presentation.ui.main.comming_soon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.proteam.fithub.databinding.FragmentCommingSoonBinding

class FragmentCommingSoon : Fragment() {
    private lateinit var binding : FragmentCommingSoonBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommingSoonBinding.inflate(inflater)
        return binding.root
    }
}