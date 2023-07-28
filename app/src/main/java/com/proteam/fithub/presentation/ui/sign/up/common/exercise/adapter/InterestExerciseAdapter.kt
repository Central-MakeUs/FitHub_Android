package com.proteam.fithub.presentation.ui.sign.up.common.exercise.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.databinding.ItemSignUpInterestSportsBinding

class InterestExerciseAdapter (
    private val onItemClicked : (Int) -> Unit
        ): RecyclerView.Adapter<InterestExerciseAdapter.InterestExerciseViewHolder>() {
    var sports = mutableListOf<ResponseExercises.ExercisesList>()
    var selected = mutableListOf<Boolean>()
    inner class InterestExerciseViewHolder(private val binding : ItemSignUpInterestSportsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : ResponseExercises.ExercisesList) {
            binding.sports = item
            binding.itemSignUpInterestSportsCheckSports.apply {
                setBackgroundResource(R.drawable.selector_sign_up_select_interest_sports)
                isSelected = selected[absoluteAdapterPosition]
                setOnClickListener {
                    resetSelected(absoluteAdapterPosition)
                    onItemClicked.invoke(item.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestExerciseViewHolder {
        return InterestExerciseViewHolder(ItemSignUpInterestSportsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = sports.size

    override fun onBindViewHolder(holder: InterestExerciseViewHolder, position: Int) {
        holder.bind(sports[position])
    }

    private fun resetSelected(position : Int) {
        for(i in 0 until selected.size) {
            selected[i] = false
        }
        setSelected(position)
    }

    private fun setSelected(position : Int) {
        selected[position] = true
        notifyItemRangeChanged(0, selected.size)
    }
}