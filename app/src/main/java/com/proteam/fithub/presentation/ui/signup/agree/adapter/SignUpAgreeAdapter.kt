package com.proteam.fithub.presentation.ui.signup.agree.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.data.data.SignUpAgreement
import com.proteam.fithub.databinding.ItemSignUpAgreementCheckboxBinding

class SignUpAgreeAdapter(private val onClicked : () -> Unit) : RecyclerView.Adapter<SignUpAgreeAdapter.SignUpAgreeViewHolder>() {
    var agreements = mutableListOf<SignUpAgreement>()
    inner class SignUpAgreeViewHolder(private val binding : ItemSignUpAgreementCheckboxBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : SignUpAgreement) {
            binding.content = item
            binding.itemFgSignUpAgreementCheckbox.setOnClickListener { onClicked.invoke() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignUpAgreeViewHolder {
        return SignUpAgreeViewHolder(ItemSignUpAgreementCheckboxBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = agreements.size

    override fun onBindViewHolder(holder: SignUpAgreeViewHolder, position: Int) {
        holder.bind(agreements[position])
    }

    fun setAllClicked(status : Boolean) {
        for (i in 0 until agreements.size) {
            agreements[i].checked = status
        }
        notifyItemRangeChanged(0, agreements.size)
    }
}