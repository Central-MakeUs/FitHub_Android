package com.proteam.fithub.presentation.ui.alarm.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.data.remote.response.ResponseAlarmData
import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.databinding.ItemRvAlarmBinding
import com.proteam.fithub.databinding.ItemRvCommunityBoardBinding

class AlarmAdapter(
    private val itemClick: (String, Int, Int) -> Unit) :
    PagingDataAdapter<ResponseAlarmData.ResultAlarmData, AlarmAdapter.AlarmViewHolder>(
        diffCallback) {

    inner class AlarmViewHolder(private val binding: ItemRvAlarmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseAlarmData.ResultAlarmData) {
            binding.itemRvAlarmTvValidate.text = if(item.alarmType == "ARTICLE") "핏사이트" else "운동인증"
            binding.data = item
            binding.root.setOnClickListener { itemClick.invoke(item.alarmType, item.targetId, item.alarmId) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        return AlarmViewHolder(
            ItemRvAlarmBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ResponseAlarmData.ResultAlarmData>() {

            override fun areItemsTheSame(oldItem: ResponseAlarmData.ResultAlarmData, newItem: ResponseAlarmData.ResultAlarmData): Boolean {
                return oldItem.alarmId == newItem.alarmId
            }

            override fun areContentsTheSame(oldItem: ResponseAlarmData.ResultAlarmData, newItem: ResponseAlarmData.ResultAlarmData): Boolean {
                return oldItem == newItem
            }
        }
    }
}