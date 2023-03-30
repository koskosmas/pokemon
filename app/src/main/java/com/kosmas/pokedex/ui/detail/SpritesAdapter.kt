package com.kosmas.pokedex.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kosmas.pokedex.databinding.ItemSpritesBinding

class SpritesAdapter : ListAdapter<String, SpritesAdapter.SpritesViewHolder>(diffUtil) {

    override fun onBindViewHolder(holder: SpritesViewHolder, position: Int) =
        holder.bindSprites(getItem(position))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpritesViewHolder =
        SpritesViewHolder(
            ItemSpritesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    inner class SpritesViewHolder constructor(
        private val binding: ItemSpritesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindSprites(sprites: String) {
            Glide.with(binding.root.context).load(sprites).into(binding.ivSprites)
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