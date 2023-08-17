package com.proteam.fithub.presentation.ui.alarm

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivityAlarmBinding
import com.proteam.fithub.presentation.ui.alarm.adapter.AlarmAdapter
import com.proteam.fithub.presentation.ui.alarm.viewmodel.AlarmViewModel
import com.proteam.fithub.presentation.ui.alarm_setting.AlarmSettingActivity
import com.proteam.fithub.presentation.ui.detail.board.BoardDetailActivity
import com.proteam.fithub.presentation.ui.detail.certificate.ExerciseCertificateDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlarmActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAlarmBinding
    private val viewModel : AlarmViewModel by viewModels()
    private val alarmAdapter by lazy {
        AlarmAdapter(::onAlarmClicked)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm)

        requestAlarmData()
        initBinding()
        initUi()
    }

    override fun onResume() {
        super.onResume()
        alarmAdapter.refresh()
    }

    private fun requestAlarmData() {
        lifecycleScope.launch {
            viewModel.requestAlarmData().collect {
                alarmAdapter.submitData(it)
            }
        }
    }

    private fun initBinding() {
        binding.activity = this
    }

    private fun initUi() {
        initAlarmRv()
    }

    private fun initAlarmRv() {
        binding.alarmRvContent.adapter = alarmAdapter

        binding.alarmRvContent.apply{
            addItemDecoration(object : ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.top = 40
                    outRect.bottom = 40
                }
            })

            addItemDecoration(object : DividerItemDecoration(this@AlarmActivity, VERTICAL) {
                override fun onDrawOver(
                    c: Canvas,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.onDrawOver(c, parent, state)
                    val paint = Paint().apply { color = resources.getColor(R.color.divider_01, null) }
                    for(i in 0 until parent.childCount - 1) {
                        val view = parent.getChildAt(i)
                        val top = view.bottom.toFloat() + 40f
                        val bottom = top + 3f

                        c.drawRect(parent.paddingLeft.toFloat(), top, parent.width - parent.paddingRight.toFloat(), bottom, paint)
                    }
                }
            })
        }
    }

    private fun onAlarmClicked(type : String, targetPk : Int, index : Int) {
        viewModel.requestAlarmRead(index)

        when(type) {
            "ARTICLE" -> openArticleActivity(targetPk)
            "RECORD" -> openRecordActivity(targetPk)
        }
    }

    private fun openArticleActivity(index : Int) {
        startActivity(Intent(this, BoardDetailActivity::class.java).setType(index.toString()))
    }

    private fun openRecordActivity(index : Int) {
        startActivity(Intent(this, ExerciseCertificateDetailActivity::class.java).setType(index.toString()))
    }

    fun openAlarmSettingActivity() {
        startActivity(Intent(this, AlarmSettingActivity::class.java))
    }
}