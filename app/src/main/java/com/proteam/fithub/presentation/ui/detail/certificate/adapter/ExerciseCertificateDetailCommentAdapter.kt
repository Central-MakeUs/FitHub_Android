package com.proteam.fithub.presentation.ui.detail.certificate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.databinding.ItemCommunityCommentBinding

class ExerciseCertificateDetailCommentAdapter
    (private val onHeartClicked : (Int) -> Unit): RecyclerView.Adapter<ExerciseCertificateDetailCommentAdapter.ExerciseCertificateDetailCommentViewHolder>() {

    inner class ExerciseCertificateDetailCommentViewHolder(private val binding : ItemCommunityCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            /** Index로 교체 **/
            binding.itemCommunityCommentIvHeart.setOnClickListener { onHeartClicked.invoke(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExerciseCertificateDetailCommentViewHolder {
        return ExerciseCertificateDetailCommentViewHolder(ItemCommunityCommentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(
        holder: ExerciseCertificateDetailCommentViewHolder,
        position: Int
    ) {
        holder.bind()
    }
}