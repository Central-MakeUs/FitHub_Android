package com.proteam.fithub.presentation.ui.write.certificate.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.databinding.ItemInterestSportsSmallBinding
import com.proteam.fithub.databinding.ItemSignUpInterestSportsBinding

class WriteOrModifyCertificateExerciseAdapter(private val onClick : (ResponseExercises.ExercisesList) -> Unit) :
    RecyclerView.Adapter<WriteOrModifyCertificateExerciseAdapter.WriteOrModifyCertificateExerciseViewHolder>() {

    var sports = mutableListOf<ResponseExercises.ExercisesList>()
    var selected = mutableListOf<Boolean>()

    inner class WriteOrModifyCertificateExerciseViewHolder(private val binding: ItemInterestSportsSmallBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseExercises.ExercisesList) {
            binding.sports = item
            binding.itemSignUpInterestSportsCheckSports.apply {
                setBackgroundResource(R.drawable.selector_sign_up_select_interest_sports)
                isSelected = selected[adapterPosition]
                setOnClickListener {
                    resetSelected(adapterPosition)
                    onClick.invoke(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WriteOrModifyCertificateExerciseViewHolder {
        return WriteOrModifyCertificateExerciseViewHolder(
            ItemInterestSportsSmallBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = sports.size

    override fun onBindViewHolder(holder: WriteOrModifyCertificateExerciseViewHolder, position: Int) {
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