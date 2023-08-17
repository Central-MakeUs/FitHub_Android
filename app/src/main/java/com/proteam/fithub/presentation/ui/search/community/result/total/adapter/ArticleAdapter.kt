package com.proteam.fithub.presentation.ui.search.community.result.total.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.databinding.ItemRvCommunityBoardBinding

class ArticleAdapter (
    private val onArticleClicked : (Int) -> Unit
        ): RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {
    var articles = mutableListOf<ResponseArticleData.ResultArticleData>()
    inner class ArticleViewHolder(private val binding : ItemRvCommunityBoardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : ResponseArticleData.ResultArticleData) {
            binding.data = item.apply { item.exerciseTag = "#${item.exerciseTag}" }
            binding.itemRvCommunityBoardLayoutUser.getUserData(item.userInfo, item.createdAt)

            binding.root.setOnClickListener { onArticleClicked.invoke(item.articleId) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(ItemRvCommunityBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = articles.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articles[position])
    }
}