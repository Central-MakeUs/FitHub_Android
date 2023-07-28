package com.proteam.fithub.presentation.ui.detail.board.image

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.data.remote.response.ResponseArticleDetailData
import com.proteam.fithub.databinding.ItemFullsizeImageBinding

class FullSizeImageAdapter : RecyclerView.Adapter<FullSizeImageAdapter.FullSizeImageViewHolder>() {
    var images = mutableListOf<ResponseArticleDetailData.ArticlePictureResult>()

    inner class FullSizeImageViewHolder(private val binding : ItemFullsizeImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : ResponseArticleDetailData.ArticlePictureResult) {
            binding.image = item.pictureUrl
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FullSizeImageViewHolder {
        return FullSizeImageViewHolder(ItemFullsizeImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: FullSizeImageViewHolder, position: Int) {
        holder.bind(images[position])
    }
}