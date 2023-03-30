package com.kosmas.pokedex.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.kosmas.pokedex.databinding.ItemDrawerSubmenuBinding

class ItemMenuCustom(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
    var binding: ItemDrawerSubmenuBinding
    init {
        val inflater = LayoutInflater.from(context)
        binding = ItemDrawerSubmenuBinding.inflate(inflater, this, true)
    }
}
