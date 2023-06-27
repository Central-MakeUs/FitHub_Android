package com.proteam.fithub.presentation.ui.signup.agree.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.databinding.ItemSignUpAgreementCheckboxBinding

class SignUpAgreeAdapter(private val contents : List<String>) : RecyclerView.Adapter<SignUpAgreeAdapter.SignUpAgreeViewHolder>() {

    inner class SignUpAgreeViewHolder(private val binding : ItemSignUpAgreementCheckboxBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : String) {
            binding.content = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignUpAgreeViewHolder {
        return SignUpAgreeViewHolder(ItemSignUpAgreementCheckboxBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = contents.size

    override fun onBindViewHolder(holder: SignUpAgreeViewHolder, position: Int) {
        holder.bind(contents[position])
    }
}