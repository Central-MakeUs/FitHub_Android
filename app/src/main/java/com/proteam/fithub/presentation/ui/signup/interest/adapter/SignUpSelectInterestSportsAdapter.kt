package com.proteam.fithub.presentation.ui.signup.interest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.databinding.ItemSignUpInterestSportsBinding

class SignUpSelectInterestSportsAdapter(private val onClick : (Int, Boolean) -> Unit) :
    RecyclerView.Adapter<SignUpSelectInterestSportsAdapter.SignUpSelectInterestSportsViewHolder>() {
    var sports = mutableListOf<ResponseExercises.ExercisesList>()
    inner class SignUpSelectInterestSportsViewHolder(private val binding: ItemSignUpInterestSportsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseExercises.ExercisesList) {
            binding.sports = item
            binding.itemSignUpInterestSportsCheckSports.apply {
                setBackgroundResource(R.drawable.selector_sign_up_select_interest_sports)
                setOnClickListener {
                    this.isSelected = this.isSelected.not()

                    onClick.invoke(item.categoryId, this.isSelected)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SignUpSelectInterestSportsViewHolder {
        return SignUpSelectInterestSportsViewHolder(
            ItemSignUpInterestSportsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = sports.size

    override fun onBindViewHolder(holder: SignUpSelectInterestSportsViewHolder, position: Int) {
        holder.bind(sports[position])
    }
}