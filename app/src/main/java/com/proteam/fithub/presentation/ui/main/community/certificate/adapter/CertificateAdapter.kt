package com.proteam.fithub.presentation.ui.main.community.certificate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.data.remote.response.ExamCertificateData
import com.proteam.fithub.databinding.ItemRvExerciseCertificateBinding

class CertificateAdapter(
    private val heartClick : (Int) -> Unit,
    private val itemClick : (Int) -> Unit
) : RecyclerView.Adapter<CertificateAdapter.CertificateViewHolder>() {
    var items = mutableListOf<ExamCertificateData>()

    inner class CertificateViewHolder(private val binding : ItemRvExerciseCertificateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : ExamCertificateData) {
            binding.data = item
            //:TODO AdapterPosition -> Index
            binding.itemRvExerciseCertificateIvHeart.setOnClickListener {
                heartClick.invoke(adapterPosition)
                changeHeartState(adapterPosition)
            }
            binding.root.setOnClickListener { itemClick.invoke(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CertificateViewHolder {
        return CertificateViewHolder(ItemRvExerciseCertificateBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CertificateViewHolder, position: Int) {
        holder.bind(items[position])
    }

    private fun changeHeartState(position : Int) {
        items[position].isHearted = items[position].isHearted.not()
        notifyItemChanged(position)
    }
}