package com.proteam.fithub.presentation.ui.search.around.result.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.data.remote.response.ResponseLocationData
import com.proteam.fithub.databinding.ComponentLocationCardBinding

class SearchAroundResultAdapter : RecyclerView.Adapter<SearchAroundResultAdapter.SearchAroundResultViewHolder>() {
    var locationData = mutableListOf<ResponseLocationData.LocationItems>()
    inner class SearchAroundResultViewHolder(private val binding : ComponentLocationCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : ResponseLocationData.LocationItems) {
            binding.data = item
            binding.componentLocationCardLayoutLevel.getExercise(item.category)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchAroundResultViewHolder {
        return SearchAroundResultViewHolder(ComponentLocationCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = locationData.size

    override fun onBindViewHolder(holder: SearchAroundResultViewHolder, position: Int) {
        holder.bind(locationData[position])
    }
}