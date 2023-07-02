package com.proteam.fithub.presentation.ui.signup.phone.dialog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.databinding.ItemDialogBottomSelectTelecomRvTelecomBinding

class SignUpPhoneNumberSelectTelecomAdapter(private val telecoms : List<String>, private val onClicked : (String) -> Unit) : RecyclerView.Adapter<SignUpPhoneNumberSelectTelecomAdapter.SignUpPhoneNumberSelectTelecomViewHolder>() {

    inner class SignUpPhoneNumberSelectTelecomViewHolder(private val binding : ItemDialogBottomSelectTelecomRvTelecomBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : String) {
            binding.telecom = item
            binding.root.setOnClickListener { onClicked.invoke(item) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SignUpPhoneNumberSelectTelecomViewHolder {
        return SignUpPhoneNumberSelectTelecomViewHolder(
            ItemDialogBottomSelectTelecomRvTelecomBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = telecoms.size

    override fun onBindViewHolder(holder: SignUpPhoneNumberSelectTelecomViewHolder, position: Int) {
        holder.bind(telecoms[position])
    }
}