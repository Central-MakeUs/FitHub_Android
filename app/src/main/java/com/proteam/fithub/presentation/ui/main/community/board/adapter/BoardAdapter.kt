package com.proteam.fithub.presentation.ui.main.community.board.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.data.data.dummy.DummyCommunityData
import com.proteam.fithub.databinding.ItemRvCommunityBoardBinding

class BoardAdapter (private val dummy : List<DummyCommunityData>, private val onBoardClick : (Int) -> Unit): RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {

    inner class BoardViewHolder(private val binding : ItemRvCommunityBoardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : DummyCommunityData) {
            binding.dummy = item
            binding.itemRvCommunityBoardLayoutUser.getUserData(item.user)

            binding.root.setOnClickListener { onBoardClick.invoke(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        return BoardViewHolder(ItemRvCommunityBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = dummy.size

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.bind(dummy[position])
    }
}