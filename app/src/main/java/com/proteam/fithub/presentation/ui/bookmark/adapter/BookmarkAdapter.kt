package com.proteam.fithub.presentation.ui.bookmark.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.databinding.ItemBookmarkNoneBinding
import com.proteam.fithub.databinding.ItemRvCommunityBoardBinding

class BookmarkAdapter(
    private val itemClick: (Int) -> Unit
) :
    PagingDataAdapter<ResponseArticleData.ResultArticleData, RecyclerView.ViewHolder>(
        diffCallback
    ) {

    inner class BookmarkViewHolder(private val binding: ItemRvCommunityBoardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseArticleData.ResultArticleData) {
            binding.data = item.apply {
                if (!this.exerciseTag.contains("#")) this.exerciseTag = "#${this.exerciseTag}"
            }
            binding.itemRvCommunityBoardLayoutUser.getUserData(item.userInfo, item.createdAt)
            binding.root.setOnClickListener { itemClick.invoke(item.articleId) }
        }
    }

    inner class NoneViewHolder(private val binding: ItemBookmarkNoneBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {

        }
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (itemCount == 0) NoneViewHolder(
            ItemBookmarkNoneBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
        else BookmarkViewHolder(
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
            is NoneViewHolder -> getItem(position)?.let { holder.bind() }
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