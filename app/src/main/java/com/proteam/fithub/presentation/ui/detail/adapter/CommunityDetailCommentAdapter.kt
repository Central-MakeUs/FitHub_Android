package com.proteam.fithub.presentation.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.data.remote.response.ResponseCommentData
import com.proteam.fithub.data.remote.response.ResponseCommentHeartClicked
import com.proteam.fithub.databinding.ItemCommunityCommentBinding

class CommunityDetailCommentAdapter
    (
    private val onHeartClicked: (Int, Int) -> Unit,
    private val onOptionClicked: (Int, Int) -> Unit
) :
    PagingDataAdapter<ResponseCommentData.ResultCommentItems, CommunityDetailCommentAdapter.CommunityDetailCommentViewHolder>(
        diffCallback
    ) {

    inner class CommunityDetailCommentViewHolder(private val binding: ItemCommunityCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseCommentData.ResultCommentItems) {
            binding.comment = item
            binding.itemCommunityCommentLayoutUser.getUserData(item.userInfo, item.createdAt)
            binding.itemCommunityCommentIvHeart.setOnClickListener {
                onHeartClicked.invoke(absoluteAdapterPosition, item.commentId)
            }
            binding.itemCommunityCommentBtnEtc.setOnClickListener { onOptionClicked.invoke(item.userInfo.ownerId, item.commentId) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommunityDetailCommentViewHolder {
        return CommunityDetailCommentViewHolder(
            ItemCommunityCommentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: CommunityDetailCommentViewHolder,
        position: Int
    ) {
        getItem(position)?.let { holder.bind(it) }
    }

    fun setHeartAction(index : Int, count : Int) {
        getItem(index)?.apply {
            isLiked = getItem(index)?.isLiked!!.not()
            likes = count
        }
        notifyItemChanged(index)
    }

    companion object {
        private val diffCallback =
            object : DiffUtil.ItemCallback<ResponseCommentData.ResultCommentItems>() {

                override fun areItemsTheSame(
                    oldItem: ResponseCommentData.ResultCommentItems,
                    newItem: ResponseCommentData.ResultCommentItems
                ): Boolean {
                    return oldItem.commentId == newItem.commentId
                }

                override fun areContentsTheSame(
                    oldItem: ResponseCommentData.ResultCommentItems,
                    newItem: ResponseCommentData.ResultCommentItems
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
