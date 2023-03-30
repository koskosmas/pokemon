package com.kosmas.pokedex.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kosmas.data.model.data.PokemonItemData
import com.kosmas.pokedex.databinding.ItemPokedexBinding
import com.kosmas.pokedex.ui.detail.TypeAdapter

class PokemonAdapter(val onItemClick: (String) -> Unit) :
    ListAdapter<PokemonItemData, PokemonAdapter.PokeDexListViewHolder>(diffUtil) {

    override fun onBindViewHolder(holder: PokeDexListViewHolder, position: Int) =
        holder.bindPokemon(getItem(position))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokeDexListViewHolder {
        return PokeDexListViewHolder(
            ItemPokedexBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    inner class PokeDexListViewHolder constructor(
        private val binding: ItemPokedexBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindPokemon(data: PokemonItemData) {
            binding.apply {
                tvPokedexName.text = data.name
                tvPokedexId.text = "# ${data.getId()}"
                rvTypes.adapter = TypeAdapter{}.apply {
                    submitList(data.types)
                }
                Glide.with(root.context).load(data.getImageUrl()).into(ivPokedex)
                root.setOnClickListener {
                    onItemClick.invoke(data.name)
                }
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<PokemonItemData>() {

            override fun areItemsTheSame(oldItem: PokemonItemData, newItem: PokemonItemData): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: PokemonItemData, newItem: PokemonItemData): Boolean =
                oldItem == newItem
        }
    }

}