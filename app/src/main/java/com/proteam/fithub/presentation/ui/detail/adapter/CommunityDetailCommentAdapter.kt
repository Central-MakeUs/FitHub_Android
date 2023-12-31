package com.proteam.fithub.presentation.ui.detail.adapter

import android.util.Log
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
    private val onOptionClicked: (Int, Int) -> Unit,
    private val userProfileClicked: (Int) -> Unit
) :
    PagingDataAdapter<ResponseCommentData.ResultCommentItems, CommunityDetailCommentAdapter.CommunityDetailCommentViewHolder>(
        diffCallback
    ) {

    inner class CommunityDetailCommentViewHolder(private val binding: ItemCommunityCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseCommentData.ResultCommentItems) {
            binding.comment = item
            binding.itemCommunityCommentLayoutUser.getUserData(item.userInfo, item.createdAt)
            binding.itemCommunityCommentIvHeart.setOnClickListener { onHeartClicked.invoke(absoluteAdapterPosition, item.commentId) }
            binding.itemCommunityCommentBtnEtc.setOnClickListener { onOptionClicked.invoke(item.userInfo.ownerId, item.commentId) }

            binding.itemCommunityCommentLayoutUser.userProfileImage().setOnClickListener { userProfileClicked.invoke(binding.itemCommunityCommentLayoutUser.userPK()) }
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

    fun getItemIndex(index : Int) : Int {
        for (i in 0 until itemCount){
            if (getItem(i)?.commentId == index) return i
        }
        return 0
    }

    fun setHeartAction(index : Int, count : Int, status : Boolean) {
        getItem(index)?.apply {
            isLiked = status
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
