package com.proteam.fithub.presentation.ui.alarm_setting

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivityAlarmSettingBinding
import com.proteam.fithub.presentation.ui.alarm_setting.viewmodel.AlarmSettingViewModel
import com.proteam.fithub.presentation.util.AnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmSettingBinding
    private val viewModel: AlarmSettingViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm_setting)

        AnalyticsHelper.setAnalyticsLog(this.javaClass.simpleName)

        observeAlarmPermitData()
        initBinding()
    }

    private fun initBinding() {
        binding.activity = this
        binding.lifecycleOwner = this
    }

    private fun observeAlarmPermitData() {
        viewModel.alarmPermitList.observe(this) {
            binding.community = it[0]
            binding.marketing = it[1]
        }
    }

    fun onCheckBoxClicked() {
        viewModel.requestModifyPermitData(
            binding.alarmSettingCheckMyWrite.isChecked,
            binding.alarmSettingCheckMarketing.isChecked
        )
    }

    fun onBackPress() {
        finish()
    }
}