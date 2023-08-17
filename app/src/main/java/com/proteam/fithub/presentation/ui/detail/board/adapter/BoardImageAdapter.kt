package com.proteam.fithub.presentation.ui.detail.board.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseArticleDetailData
import com.proteam.fithub.databinding.ItemRvCommunityBoardImageBinding

class BoardImageAdapter (
    private val onImageClick : (String) -> Unit
        ) : RecyclerView.Adapter<BoardImageAdapter.BoardImageViewHolder>() {
    var images = mutableListOf<ResponseArticleDetailData.ArticlePictureResult>()
    inner class BoardImageViewHolder(private val binding : ItemRvCommunityBoardImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : ResponseArticleDetailData.ArticlePictureResult) {
            binding.path = item.pictureUrl
            binding.root.setOnClickListener { onImageClick.invoke(item.pictureUrl) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardImageViewHolder {
        return BoardImageViewHolder(ItemRvCommunityBoardImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int  = images.size

    override fun onBindViewHolder(holder: BoardImageViewHolder, position: Int) {
        holder.bind(images[position])
    }
}