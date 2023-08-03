package com.proteam.fithub.presentation.ui.sign.up.common.agreement.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.data.data.Agreements
import com.proteam.fithub.databinding.ItemSignUpAgreementCheckboxBinding

class AgreementAdapter(private val onClicked : () -> Unit) : RecyclerView.Adapter<AgreementAdapter.AgreementViewHolder>() {
    var agreements = mutableListOf<Agreements>()

    inner class AgreementViewHolder(private val binding : ItemSignUpAgreementCheckboxBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : Agreements) {
            binding.content = item
            binding.itemFgSignUpAgreementCheckbox.setOnClickListener { onClicked.invoke() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgreementViewHolder {
        return AgreementViewHolder(ItemSignUpAgreementCheckboxBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = agreements.size

    override fun onBindViewHolder(holder: AgreementViewHolder, position: Int) {
        holder.bind(agreements[position])
    }

    fun setAllClicked(status : Boolean) {
        for (i in 0 until agreements.size) {
            agreements[i].checked = status
        }
        notifyItemRangeChanged(0, agreements.size)
    }
}