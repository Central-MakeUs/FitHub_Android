package com.proteam.fithub.presentation.ui.manageinfo

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivityManageMyInfoBinding
import com.proteam.fithub.presentation.ui.manageinfo.viewmodel.ManageMyInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageMyInfoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityManageMyInfoBinding
    private val viewModel : ManageMyInfoViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_my_info)

        observeMyInfoData()
        initBinding()
    }

    private fun initBinding() {
        binding.activity = this
        binding.lifecycleOwner = this
    }

    private fun observeMyInfoData() {
        viewModel.myInfoData.observe(this) {
            binding.data = it
        }
    }

    fun onBackPress() {
        finish()
    }
}