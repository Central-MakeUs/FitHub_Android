package com.proteam.fithub.presentation.ui.main.community.certificate.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.data.remote.response.ExamCertificateData
import com.proteam.fithub.data.remote.response.ResponseCertificateData
import com.proteam.fithub.databinding.ItemRvExerciseCertificateBinding

class CertificateAdapter(
    private val heartClick : (Int) -> Unit,
    private val itemClick : (Int) -> Unit
) : PagingDataAdapter<ResponseCertificateData.ResultCertificateData, CertificateAdapter.CertificateViewHolder>(
    diffCallback) {
    inner class CertificateViewHolder(private val binding : ItemRvExerciseCertificateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : ResponseCertificateData.ResultCertificateData) {
            binding.data = item
            binding.itemRvExerciseCertificateIvHeart.setOnClickListener {
                heartClick.invoke(item.recordId)
                changeHeartState(absoluteAdapterPosition)
            }
            binding.root.setOnClickListener { itemClick.invoke(getItem(position)!!.recordId) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CertificateViewHolder {
        return CertificateViewHolder(ItemRvExerciseCertificateBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CertificateViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    fun changeHeartState(position : Int) {
        getItem(position)?.isLiked = getItem(position)?.isLiked!!.not()
        getItem(position)?.likes = if(getItem(position)?.isLiked == true) getItem(position)?.likes?.plus(1)!! else getItem(position)?.likes?.minus(1)!!
        notifyItemChanged(position)
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ResponseCertificateData.ResultCertificateData>() {

            override fun areItemsTheSame(oldItem: ResponseCertificateData.ResultCertificateData, newItem: ResponseCertificateData.ResultCertificateData): Boolean {
                return oldItem.recordId == newItem.recordId
            }

            override fun areContentsTheSame(oldItem: ResponseCertificateData.ResultCertificateData, newItem: ResponseCertificateData.ResultCertificateData): Boolean {
                return oldItem == newItem
            }
        }
    }
}