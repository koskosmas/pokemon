package com.kosmas.pokedex.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kosmas.pokedex.databinding.ItemTypeBinding
import com.kosmas.core_ui.utils.PokemonColorsUtils

class TypeAdapter(val onItemClick: (String) -> Unit)  : ListAdapter<String, TypeAdapter.ContentViewHolder>(diffUtil) {

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) =
        holder.bindType(getItem(position))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        return ContentViewHolder(
            ItemTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    inner class ContentViewHolder constructor(
        private val binding: ItemTypeBinding
    ) : ViewHolder(binding.root) {

        fun bindType(typeName: String) {
            binding.apply {
                tvType.text = typeName
                cvTypesContainter.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        PokemonColorsUtils.getTypeColor(typeName)
                    )
                )
                root.setOnClickListener { onItemClick.invoke(typeName) }
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<String>() {

            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }
    }
}