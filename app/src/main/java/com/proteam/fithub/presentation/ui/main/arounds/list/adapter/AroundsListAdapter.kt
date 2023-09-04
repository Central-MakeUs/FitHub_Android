package com.proteam.fithub.presentation.ui.main.arounds.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.data.remote.response.ResponseLocationData
import com.proteam.fithub.databinding.ComponentLocationCardBinding

class AroundsListAdapter (
    private val onItemClicked : (ResponseLocationData.LocationItems) -> Unit
        ) : RecyclerView.Adapter<AroundsListAdapter.AroundsListViewHolder>() {
    private var items = mutableListOf<ResponseLocationData.LocationItems>()

    inner class AroundsListViewHolder(private val binding : ComponentLocationCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : ResponseLocationData.LocationItems) {
            binding.data = item
            binding.componentLocationCardLayoutLevel.getExercise(item.category)

            binding.root.setOnClickListener { onItemClicked.invoke(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AroundsListViewHolder {
        return AroundsListViewHolder(ComponentLocationCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: AroundsListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(list : MutableList<ResponseLocationData.LocationItems>) {
        items = list
        notifyDataSetChanged()
    }
}