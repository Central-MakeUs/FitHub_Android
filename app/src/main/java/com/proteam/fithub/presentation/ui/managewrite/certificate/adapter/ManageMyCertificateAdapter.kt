package com.proteam.fithub.presentation.ui.managewrite.certificate.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.data.remote.response.ResponseCertificateData
import com.proteam.fithub.data.remote.response.ResponseMyCertificateData
import com.proteam.fithub.databinding.ItemRvExerciseCertificateBinding
import com.proteam.fithub.databinding.ItemRvMyCertificateBinding

class ManageMyCertificateAdapter(
    private val itemClick : (Int) -> Unit,
    private val checkClick : (Int, Boolean) -> Unit
) : PagingDataAdapter<ResponseMyCertificateData.ResultMyCertificateData, ManageMyCertificateAdapter.MyCertificateViewHolder>(
    diffCallback) {
    inner class MyCertificateViewHolder(private val binding : ItemRvMyCertificateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : ResponseMyCertificateData.ResultMyCertificateData) {
            binding.data = item
            binding.root.setOnClickListener { itemClick.invoke(item.recordId) }
            binding.itemRvMyCertificateCheckSelect.setOnClickListener {
                item.isSelected = !it.isSelected
                checkClick.invoke(item.recordId, item.isSelected)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageMyCertificateAdapter.MyCertificateViewHolder {
        return MyCertificateViewHolder(ItemRvMyCertificateBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyCertificateViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ResponseMyCertificateData.ResultMyCertificateData>() {

            override fun areItemsTheSame(oldItem: ResponseMyCertificateData.ResultMyCertificateData, newItem: ResponseMyCertificateData.ResultMyCertificateData): Boolean {
                return oldItem.recordId == newItem.recordId
            }

            override fun areContentsTheSame(oldItem: ResponseMyCertificateData.ResultMyCertificateData, newItem: ResponseMyCertificateData.ResultMyCertificateData): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun checkAll(selection : Boolean) : List<Int> {
        val list = mutableListOf<Int>()
        for(i in 0 until itemCount) {
            getItem(i)?.isSelected = selection
            list.add(getItem(i)!!.recordId)
        }
        notifyItemRangeChanged(0, itemCount)
        return list
    }
}