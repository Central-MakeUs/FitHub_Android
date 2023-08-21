package com.proteam.fithub.presentation.ui.terms.terms.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.databinding.ItemRvTermsBinding

class TermsAdapter(private val titles : List<String>, private val onClick : (Int) -> Unit) : RecyclerView.Adapter<TermsAdapter.TermsViewHolder>() {

    inner class TermsViewHolder(private val binding: ItemRvTermsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : String) {
            binding.itemRvTermsTvTitle.text = item
            binding.itemRvTermsTvTitle.setOnClickListener { onClick.invoke(absoluteAdapterPosition + 1) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermsViewHolder {
        return TermsViewHolder(ItemRvTermsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = titles.size

    override fun onBindViewHolder(holder: TermsViewHolder, position: Int) {
        holder.bind(titles[position])
    }
}