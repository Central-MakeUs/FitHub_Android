package com.proteam.fithub.presentation.ui.main.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.databinding.ItemInterestSportsSmallBinding

class HomeNearGymAdapter(
    private val onGymClicked : (Int) -> Unit
) : RecyclerView.Adapter<HomeNearGymAdapter.HomeNearGymViewHolder>() {
    var exercises = mutableListOf<ResponseExercises.ExercisesList>()
    inner class HomeNearGymViewHolder(private val binding : ItemInterestSportsSmallBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : ResponseExercises.ExercisesList) {
            binding.sports = item
            binding.itemSignUpInterestSportsCheckSports.apply {
                setBackgroundResource(R.drawable.selector_sign_up_select_interest_sports)
            }
            binding.root.setOnClickListener { onGymClicked.invoke(item.id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeNearGymViewHolder {
        return HomeNearGymViewHolder(ItemInterestSportsSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = exercises.size

    override fun onBindViewHolder(holder: HomeNearGymViewHolder, position: Int) {
        holder.bind(exercises[position])
    }
}