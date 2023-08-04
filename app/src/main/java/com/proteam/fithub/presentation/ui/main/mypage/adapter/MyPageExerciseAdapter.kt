package com.proteam.fithub.presentation.ui.main.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.data.remote.response.ResponseMyPageData
import com.proteam.fithub.databinding.ItemMypageVpExercisesBinding

class MyPageExerciseAdapter : RecyclerView.Adapter<MyPageExerciseAdapter.MyPageExerciseViewHolder>() {
    var exercises = mutableListOf<ResponseMyPageData.ExerciseData>()
    inner class MyPageExerciseViewHolder(private val binding : ItemMypageVpExercisesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : ResponseMyPageData.ExerciseData) {
            binding.data = item
            binding.title = if(absoluteAdapterPosition == 0) "메인 운동" else "서브 운동"

            binding.itemMypageVpExercisesComponentExercise.getExercise(item.category)
            binding.itemMypageVpExercisesComponentLevel.getLevel(item.level, item.gradeName)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageExerciseViewHolder {
        return MyPageExerciseViewHolder(ItemMypageVpExercisesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = exercises.size

    override fun onBindViewHolder(holder: MyPageExerciseViewHolder, position: Int) {
        holder.bind(exercises[position])
    }
}