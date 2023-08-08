package com.proteam.fithub.presentation.ui.main.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.databinding.ItemMypageRvMenuBinding

class MyPageUpperMenuAdapter(private val menus : List<String>, private val onClick : (Int) -> Unit) : RecyclerView.Adapter<MyPageUpperMenuAdapter.MyPageUpperMenuViewHolder>() {

    inner class MyPageUpperMenuViewHolder(private val binding : ItemMypageRvMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : String) {
            binding.title = item
            binding.root.setOnClickListener { onClick.invoke(absoluteAdapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageUpperMenuViewHolder {
        return MyPageUpperMenuViewHolder(ItemMypageRvMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = menus.size

    override fun onBindViewHolder(holder: MyPageUpperMenuViewHolder, position: Int) {
        holder.bind(menus[position])
    }
}