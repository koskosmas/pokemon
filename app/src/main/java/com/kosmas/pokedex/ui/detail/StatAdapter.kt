package com.kosmas.pokedex.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kosmas.pokedex.R
import com.kosmas.domain.model.StatDomainModel
import com.kosmas.pokedex.databinding.ItemStatBinding
import com.kosmas.core_ui.utils.PokemonColorsUtils

class StatAdapter : ListAdapter<StatDomainModel, StatAdapter.StatViewHolder>(diffUtil) {

    override fun onBindViewHolder(holder: StatViewHolder, position: Int) =
        holder.bindSprites(getItem(position))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatViewHolder =
        StatViewHolder(
            ItemStatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    inner class StatViewHolder constructor(
        private val binding: ItemStatBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindSprites(stat: StatDomainModel) {
            binding.apply {
                tvValue.text = stat.value
                tvValue.setTextColor(
                    AppCompatResources.getColorStateList(
                        tvValue.context,
                        PokemonColorsUtils.getStatColor(stat.name)
                    )
                )
                tvName.text = stat.name
                setBackground(clStatContainer, stat.name)
            }
        }
    }

    private fun setBackground(view: View, stat: String) {
        when (stat) {
            "hp" -> view.setBackgroundResource(R.drawable.shape_circle_red)
            "defense" -> view.setBackgroundResource(R.drawable.shape_circle_orange)
            "special-defense" -> view.setBackgroundResource(R.drawable.shape_cricle_green)
            "attack" -> view.setBackgroundResource(R.drawable.shape_circle_blue)
            "special-attack" -> view.setBackgroundResource(R.drawable.shape_circle_yellow)
            else -> view.setBackgroundResource(R.drawable.shape_circle_ghost)
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<StatDomainModel>() {

            override fun areItemsTheSame(oldItem: StatDomainModel, newItem: StatDomainModel): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: StatDomainModel, newItem: StatDomainModel): Boolean =
                oldItem == newItem
        }
    }
}