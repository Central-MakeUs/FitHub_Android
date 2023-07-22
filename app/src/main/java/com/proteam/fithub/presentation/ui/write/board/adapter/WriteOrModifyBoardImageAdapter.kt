package com.proteam.fithub.presentation.ui.write.board.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.databinding.ItemWriteBoardImagesBinding

class WriteOrModifyBoardImageAdapter(private val onDeleteClicked : (Int) -> Unit) : RecyclerView.Adapter<WriteOrModifyBoardImageAdapter.WriteOrModifyBoardImageViewHolder>() {
    var paths = mutableListOf<Uri>()
    inner class WriteOrModifyBoardImageViewHolder(private val binding : ItemWriteBoardImagesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : Uri) {
            binding.path = item
            binding.itemWriteBoardImagesBtnDelete.setOnClickListener { onDeleteClicked.invoke(absoluteAdapterPosition) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WriteOrModifyBoardImageViewHolder {
        return WriteOrModifyBoardImageViewHolder(ItemWriteBoardImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = paths.size

    override fun onBindViewHolder(holder: WriteOrModifyBoardImageViewHolder, position: Int) {
        holder.bind(paths[position])
    }
}