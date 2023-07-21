package com.proteam.fithub.presentation.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.R
import com.proteam.fithub.data.data.ComponentUserData
import com.proteam.fithub.databinding.ItemCommunityCommentBinding

class CommunityDetailCommentAdapter
    (private val onHeartClicked : (Int) -> Unit): RecyclerView.Adapter<CommunityDetailCommentAdapter.CommunityDetailCommentViewHolder>() {

    inner class CommunityDetailCommentViewHolder(private val binding : ItemCommunityCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {

            binding.itemCommunityCommentLayoutUser.getUserData(
                ComponentUserData(
                    R.drawable.ic_launcher_background,
                    "춘배",
                    "${adapterPosition}분 전",
                    "크로스핏",
                    (adapterPosition + 1).toString()
                )
            )

            /** Index로 교체 **/
            binding.itemCommunityCommentIvHeart.setOnClickListener { onHeartClicked.invoke(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommunityDetailCommentViewHolder {
        return CommunityDetailCommentViewHolder(ItemCommunityCommentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(
        holder: CommunityDetailCommentViewHolder,
        position: Int
    ) {
        holder.bind()
    }
}