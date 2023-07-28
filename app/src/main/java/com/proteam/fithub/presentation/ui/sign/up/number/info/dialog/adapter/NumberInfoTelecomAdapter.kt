package com.proteam.fithub.presentation.ui.sign.up.number.info.dialog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.databinding.ItemDialogBottomSelectTelecomRvTelecomBinding

class NumberInfoTelecomAdapter(private val telecoms : List<String>, private val onClicked : (String) -> Unit) : RecyclerView.Adapter<NumberInfoTelecomAdapter.NumberInfoTelecomViewHolder>() {

    inner class NumberInfoTelecomViewHolder(private val binding : ItemDialogBottomSelectTelecomRvTelecomBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : String) {
            binding.telecom = item
            binding.root.setOnClickListener { onClicked.invoke(item) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NumberInfoTelecomViewHolder {
        return NumberInfoTelecomViewHolder(
            ItemDialogBottomSelectTelecomRvTelecomBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = telecoms.size

    override fun onBindViewHolder(holder: NumberInfoTelecomViewHolder, position: Int) {
        holder.bind(telecoms[position])
    }
}