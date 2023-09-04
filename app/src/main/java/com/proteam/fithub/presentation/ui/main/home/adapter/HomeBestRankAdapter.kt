package com.proteam.fithub.presentation.ui.main.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseHomeData
import com.proteam.fithub.databinding.ItemHomeBestRankingBinding

class HomeBestRankAdapter (
    private val onProfileClicked: (Int) -> Unit
        ): RecyclerView.Adapter<HomeBestRankAdapter.HomeBestRankViewHolder>() {
    var rankingData = mutableListOf<ResponseHomeData.RecorderItems>()

    inner class HomeBestRankViewHolder(private val binding: ItemHomeBestRankingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseHomeData.RecorderItems) {
            binding.rankingData = item
            binding.itemHomeBestRankingIvCrown.visibility =
                if (item.ranking == 1) View.VISIBLE else View.GONE
            binding.itemHomeBestRankingLayoutLevel.getLevel(item.level, item.gradeName)
            binding.itemHomeBestRankingLayoutExercise.getExercise(item.category)

            //:TODO 랭커인덱스 받아서 유저상세 넣기
            binding.itemHomeBestRankingIvProfile.setOnClickListener { onProfileClicked.invoke(item.id) }

            binding.itemHomeBestRankingIvRate.setImageResource(
                when (item.rankingStatus) {
                    "KEEP" -> R.drawable.ic_rank_keep
                    "UP" -> R.drawable.ic_rank_upper
                    "DOWN" -> R.drawable.ic_rank_down
                    "NEW" -> R.drawable.ic_rank_new
                    else -> com.bumptech.glide.R.drawable.abc_list_selector_background_transition_holo_dark
                }
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeBestRankViewHolder {
        return HomeBestRankViewHolder(
            ItemHomeBestRankingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = rankingData.size

    override fun onBindViewHolder(holder: HomeBestRankViewHolder, position: Int) {
        holder.bind(rankingData[position])
    }
}