package com.kosmas.pokedex.ui.pokemontype

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kosmas.pokedex.R
import com.kosmas.pokedex.databinding.ItemPaginationBinding
import com.kosmas.pokedex.model.PaginationModel

class PaginationAdapter(val onItemClick: (String, Int) -> Unit) :
    ListAdapter<PaginationModel, PaginationAdapter.PokemonListViewHolder>(diffUtil) {

    override fun onBindViewHolder(holder: PokemonListViewHolder, position: Int) =
        holder.bindPokemonData(getItem(position), position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonListViewHolder {
        return PokemonListViewHolder(
            ItemPaginationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    inner class PokemonListViewHolder constructor(
        private val binding: ItemPaginationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindPokemonData(data: PaginationModel, position: Int) {
            binding.apply {
                root.setOnClickListener {
                    onItemClick.invoke(data.pagination, position)
                }
                btnNavigation.apply {
                    background = if (data.isActive) {
                        setTextColor(ContextCompat.getColor(root.context, com.kosmas.core_ui.R.color.white))
                        ContextCompat.getDrawable(
                            root.context,
                            R.drawable.bg_pagination_filled
                        )
                    } else {
                        setTextColor(ContextCompat.getColor(root.context, com.kosmas.core_ui.R.color.blue_700))
                        ContextCompat.getDrawable(
                            root.context,
                            R.drawable.bg_pagination
                        )
                    }
                    text = data.pagination
                }
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<PaginationModel>() {
            override fun areItemsTheSame(
                oldItem: PaginationModel,
                newItem: PaginationModel
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: PaginationModel,
                newItem: PaginationModel
            ): Boolean = oldItem == newItem
        }
    }
}