package com.proteam.fithub.presentation.ui.search.result.total.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.data.remote.response.ResponseCertificateData
import com.proteam.fithub.databinding.ItemRvResultTotalCertificateBinding

class CertificateAdapter(
    private val onCertificateClicked : (Int) -> Unit
) : RecyclerView.Adapter<CertificateAdapter.CertificateViewHolder>() {
    var certificates = mutableListOf<ResponseCertificateData.ResultCertificateData>()
    inner class CertificateViewHolder(private val binding : ItemRvResultTotalCertificateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : ResponseCertificateData.ResultCertificateData) {
            binding.data = item
            binding.root.setOnClickListener { onCertificateClicked.invoke(item.recordId) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CertificateViewHolder {
        return CertificateViewHolder(ItemRvResultTotalCertificateBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = certificates.size

    override fun onBindViewHolder(holder: CertificateViewHolder, position: Int) {
        holder.bind(certificates[position])
    }
}