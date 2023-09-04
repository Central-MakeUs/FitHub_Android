package com.proteam.fithub.presentation.ui.bookmark.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.databinding.ItemRvCommunityBoardBinding

class BookmarkAdapter(
    private val itemClick: (Int) -> Unit
) :
    PagingDataAdapter<ResponseArticleData.ResultArticleData, RecyclerView.ViewHolder>(
        diffCallback
    ) {

    inner class BookmarkViewHolder(private val binding: ItemRvCommunityBoardBinding) : ViewHolder(binding.root) {
        fun bind(item: ResponseArticleData.ResultArticleData) {
            binding.data = item.apply { this.exerciseTag?.let { if(!it.contains("#")) this.exerciseTag = "#${this.exerciseTag}" } }
            binding.itemRvCommunityBoardTvTag.visibility = if(item.exerciseTag == null) View.GONE else View.VISIBLE
            binding.itemRvCommunityBoardLayoutUser.getUserData(item.userInfo, item.createdAt)
            binding.root.setOnClickListener { itemClick.invoke(item.articleId) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BookmarkViewHolder(
            ItemRvCommunityBoardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is BookmarkViewHolder -> getItem(position)?.let { holder.bind(it) }
        }

    }

    companion object {
        private val diffCallback =
            object : DiffUtil.ItemCallback<ResponseArticleData.ResultArticleData>() {

                override fun areItemsTheSame(
                    oldItem: ResponseArticleData.ResultArticleData,
                    newItem: ResponseArticleData.ResultArticleData
                ): Boolean {
                    return oldItem.articleId == newItem.articleId
                }

                override fun areContentsTheSame(
                    oldItem: ResponseArticleData.ResultArticleData,
                    newItem: ResponseArticleData.ResultArticleData
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}