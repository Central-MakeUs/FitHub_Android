package com.proteam.fithub.presentation.ui.gallery.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.proteam.fithub.presentation.ui.gallery.viewmodel.CustomGalleryViewModel
import com.google.android.material.snackbar.Snackbar
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivityCustomGalleryBinding
import com.proteam.fithub.presentation.ui.gallery.adapter.GalleryAdapter

class CustomGalleryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCustomGalleryBinding
    private val viewModel : CustomGalleryViewModel by viewModels()
    private val galleryAdapter by lazy {
        GalleryAdapter(::onImageClick, ::snackbarWhenOverLength)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_custom_gallery)

        initBinding()
        initRV()
        getGalleries()
        observe()
    }

    private fun initBinding() {
        binding.activity = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun initRV() {
        binding.customGalleryRvImages.adapter = galleryAdapter
    }

    private fun getGalleries() {
        viewModel.fetchImageItemList(this)
    }

    private fun observe() {
        viewModel.imageItemList.observe(this) {
            galleryAdapter.galleryData = it
        }
    }

    private fun onImageClick(position : Int) {
        viewModel.changeImageClicked(position)
    }

    private fun snackbarWhenOverLength() {
        Snackbar.make(binding.root, "사진은 최대 10장까지 선택할 수 있습니다.", Snackbar.LENGTH_SHORT).show()
    }

    fun setResult() {
        setResult(Activity.RESULT_OK, Intent().putExtra("Images", viewModel.getCheckedImageUriList() as ArrayList))
        finish()
    }
}