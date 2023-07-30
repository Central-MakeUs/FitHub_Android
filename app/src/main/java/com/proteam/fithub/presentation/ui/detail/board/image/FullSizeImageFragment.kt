package com.proteam.fithub.presentation.ui.detail.board.image

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentFullsizeImageBinding
import com.proteam.fithub.presentation.ui.detail.board.viewmodel.BoardDetailViewModel

class FullSizeImageFragment : Fragment() {
    private lateinit var binding : FragmentFullsizeImageBinding
    private val viewModel : BoardDetailViewModel by activityViewModels()

    private val fullSizeImageAdapter by lazy {
        FullSizeImageAdapter()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fullsize_image, container, false)

        initBinding()
        initRV()

        return binding.root
    }

    private fun initBinding() {
        binding.fragment = this
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                dismissClicked()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun initRV() {
        /*binding.fgFullsizeImageVpImages.adapter = fullSizeImageAdapter
        fullSizeImageAdapter.images = viewModel.articleData.value?.articlePictureList?.pictureList as MutableList
        binding.fgFullsizeImageVpImages.setCurrentItem(tag!!.toInt(), false) */
    }

    fun dismissClicked() {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
    }

}