package com.proteam.fithub.presentation.ui.managewrite.article.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.data.remote.response.ResponseMyArticleData
import com.proteam.fithub.databinding.ItemRvMyBoardBinding

class ManageMyArticleAdapter(
    private val itemClick : (Int) -> Unit,
    private val checkClick : (Int, Boolean) -> Unit
) : PagingDataAdapter<ResponseMyArticleData.ResultMyArticleData, ManageMyArticleAdapter.MyArticleViewHolder>(
    diffCallback) {
    inner class MyArticleViewHolder(private val binding : ItemRvMyBoardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : ResponseMyArticleData.ResultMyArticleData) {
            binding.data = item.apply { if(!this.exerciseTag.contains("#")) this.exerciseTag = "#${this.exerciseTag}" }
            binding.root.setOnClickListener { itemClick.invoke(item.articleId) }
            binding.itemRvMyArticleCheckSelect.setOnClickListener {
                item.isSelected = !it.isSelected
                checkClick.invoke(item.articleId, item.isSelected)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageMyArticleAdapter.MyArticleViewHolder {
        return MyArticleViewHolder(ItemRvMyBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyArticleViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ResponseMyArticleData.ResultMyArticleData>() {

            override fun areItemsTheSame(oldItem: ResponseMyArticleData.ResultMyArticleData, newItem: ResponseMyArticleData.ResultMyArticleData): Boolean {
                return oldItem.articleId == newItem.articleId
            }

            override fun areContentsTheSame(oldItem: ResponseMyArticleData.ResultMyArticleData, newItem: ResponseMyArticleData.ResultMyArticleData): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun checkAll(selection : Boolean) : List<Int> {
        val list = mutableListOf<Int>()
        for(i in 0 until itemCount) {
            getItem(i)?.isSelected = selection
            list.add(getItem(i)!!.articleId)
        }
        notifyItemRangeChanged(0, itemCount)
        return list
    }
}