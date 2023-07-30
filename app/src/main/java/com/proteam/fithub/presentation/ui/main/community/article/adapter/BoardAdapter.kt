package com.proteam.fithub.presentation.ui.main.community.article.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.databinding.ItemRvCommunityBoardBinding

class BoardAdapter(
    private val itemClick: (Int) -> Unit) :
    PagingDataAdapter<ResponseArticleData.ResultArticleData, BoardAdapter.BoardViewHolder>(
        diffCallback) {

    inner class BoardViewHolder(private val binding: ItemRvCommunityBoardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseArticleData.ResultArticleData) {
            binding.data = item.apply { this.exerciseTag = "#${this.exerciseTag}" }
            binding.itemRvCommunityBoardLayoutUser.getUserData(item.userInfo, item.createdAt)
            binding.root.setOnClickListener { itemClick.invoke(item.articleId) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        return BoardViewHolder(
            ItemRvCommunityBoardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
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