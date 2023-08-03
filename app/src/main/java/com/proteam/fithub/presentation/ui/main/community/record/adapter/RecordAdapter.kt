package com.proteam.fithub.presentation.ui.main.community.record.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.data.remote.response.ExamCertificateData
import com.proteam.fithub.data.remote.response.ResponseCertificateData
import com.proteam.fithub.databinding.ItemRvExerciseCertificateBinding

class RecordAdapter(
    private val itemClick : (Int) -> Unit
) : PagingDataAdapter<ResponseCertificateData.ResultCertificateData, RecordAdapter.CertificateViewHolder>(
    diffCallback) {
    inner class CertificateViewHolder(private val binding : ItemRvExerciseCertificateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : ResponseCertificateData.ResultCertificateData) {
            binding.data = item
            binding.root.setOnClickListener { itemClick.invoke(getItem(position)!!.recordId) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CertificateViewHolder {
        return CertificateViewHolder(ItemRvExerciseCertificateBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CertificateViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
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