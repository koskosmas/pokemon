package com.kosmas.pokedex.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kosmas.domain.model.EvolutionDomainModel
import com.kosmas.pokedex.R
import com.kosmas.pokedex.databinding.ItemEvolutionBinding

class EvolutionAdapter(val onItemClick: (String) -> Unit) :
    ListAdapter<EvolutionDomainModel, EvolutionAdapter.StatViewHolder>(diffUtil) {

    override fun onBindViewHolder(holder: StatViewHolder, position: Int) =
        holder.bindEvolution(getItem(position), position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatViewHolder =
        StatViewHolder(
            ItemEvolutionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    inner class StatViewHolder constructor(
        private val binding: ItemEvolutionBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindEvolution(evolution: EvolutionDomainModel, position: Int) {
            binding.apply {
                if (position > 0) setBackground(llEvolutionContainer, position, ivArrow)
                Glide.with(root.context).load(evolution.url).into(ivEvolution)
                tvName.text = evolution.name
                root.setOnClickListener { onItemClick(evolution.name) }
            }
        }
    }

    private fun setBackground(view: View, position: Int, arrowImage: AppCompatImageView) {
        when (position) {
            1 -> view.setBackgroundResource(R.drawable.shape_evolution_2)
            2 -> view.setBackgroundResource(R.drawable.shape_evolution_3)
            else -> {
                arrowImage.isVisible = false
                view.setBackgroundResource(R.drawable.shape_evolution_4)
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<EvolutionDomainModel>() {

            override fun areItemsTheSame(oldItem: EvolutionDomainModel, newItem: EvolutionDomainModel): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: EvolutionDomainModel, newItem: EvolutionDomainModel): Boolean =
                oldItem == newItem
        }
    }
}