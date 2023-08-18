package com.proteam.fithub.presentation.ui.otheruser.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.databinding.ItemRvCommunityBoardBinding
import com.proteam.fithub.databinding.ItemRvCommunityBoardWithoutUserBinding

class OtherUserArticleAdapter(
    private val itemClick: (Int) -> Unit) :
    PagingDataAdapter<ResponseArticleData.ResultArticleData, OtherUserArticleAdapter.OtherUserArticleViewHolder>(
        diffCallback) {

    inner class OtherUserArticleViewHolder(private val binding: ItemRvCommunityBoardWithoutUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseArticleData.ResultArticleData) {
            binding.data = item.apply { this.exerciseTag?.let { if(!it.contains("#")) this.exerciseTag = "#${this.exerciseTag}" } }
            binding.itemRvCommunityBoardTvTag.visibility = if(item.exerciseTag == null) View.GONE else View.VISIBLE
            binding.root.setOnClickListener { itemClick.invoke(item.articleId) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherUserArticleAdapter.OtherUserArticleViewHolder {
        return OtherUserArticleViewHolder(
            ItemRvCommunityBoardWithoutUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: OtherUserArticleViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ResponseArticleData.ResultArticleData>() {

            override fun areItemsTheSame(oldItem: ResponseArticleData.ResultArticleData, newItem: ResponseArticleData.ResultArticleData): Boolean {
                return oldItem.articleId == newItem.articleId
            }

            override fun areContentsTheSame(oldItem: ResponseArticleData.ResultArticleData, newItem: ResponseArticleData.ResultArticleData): Boolean {
                return oldItem == newItem
            }
        }
    }
}