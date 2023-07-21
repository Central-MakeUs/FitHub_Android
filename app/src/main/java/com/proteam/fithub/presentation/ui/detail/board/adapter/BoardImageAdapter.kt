package com.proteam.fithub.presentation.ui.detail.board.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ItemRvCommunityBoardImageBinding

class BoardImageAdapter : RecyclerView.Adapter<BoardImageAdapter.BoardImageViewHolder>() {

    inner class BoardImageViewHolder(private val binding : ItemRvCommunityBoardImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            /** String **/
            binding.path = R.drawable.ic_launcher_background
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardImageViewHolder {
        return BoardImageViewHolder(ItemRvCommunityBoardImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int  = 5

    override fun onBindViewHolder(holder: BoardImageViewHolder, position: Int) {
        holder.bind()
    }
}