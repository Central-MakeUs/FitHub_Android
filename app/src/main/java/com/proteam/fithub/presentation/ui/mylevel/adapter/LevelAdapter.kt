package com.proteam.fithub.presentation.ui.mylevel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.data.remote.response.ResponseMyLevelData
import com.proteam.fithub.databinding.ItemMyLevelAllLevelBinding

class LevelAdapter : RecyclerView.Adapter<LevelAdapter.LevelViewHolder>() {
    var levels = mutableListOf<ResponseMyLevelData.FithubLevelDetail>()
    inner class LevelViewHolder(private val binding : ItemMyLevelAllLevelBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : ResponseMyLevelData.FithubLevelDetail) {
            binding.path = item.levelIconUrl
            binding.itemMyLevelAllLevelComponentLevel.getLevel(item.level, item.levelName)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        return LevelViewHolder(ItemMyLevelAllLevelBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = levels.size

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        holder.bind(levels[position])
    }
}