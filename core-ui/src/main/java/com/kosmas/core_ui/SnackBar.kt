package com.kosmas.core_ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar

class SnackBar(
    parent: ViewGroup,
    content: SnackBarView
) : BaseTransientBottomBar<SnackBar>(parent, content, content) {
    private var viewer: View = getView()

    companion object {
        fun make(view: View, isVisibleBtn: Boolean = true): SnackBar {
            val parent = view.findSuitableParent() ?: throw IllegalArgumentException(
                "No suitable parent found from the given view. Please provide a valid view."
            )
            val customView = LayoutInflater.from(view.context).inflate(
                R.layout.custom_snackbar_singleline,
                parent,
                false
            ) as SnackBarView

            if(!isVisibleBtn){
                (customView.findViewById<View>(R.id.snack_button) as TextView?)?.visibility = View.GONE
            }
            return SnackBar(
                parent,
                customView
            )
        }
    }

    fun setBackgroundResource(drawable: Int): SnackBar {
        viewer.setBackgroundResource(drawable)
        return this
    }

    fun setBackgroundColor(color: Int): SnackBar {
        viewer.setBackgroundColor(ContextCompat.getColor(view.context, color))
        return this
    }

    fun setTextColor(color: Int): SnackBar {
        viewer.findViewById<TextView>(R.id.snack_title).setTextColor(color)
        viewer.findViewById<TextView>(R.id.snack_button).setTextColor(color)
        return this
    }

    fun setPadding(left: Int, top: Int, right: Int, bottom: Int): SnackBar {
        viewer.setPadding(left, top, right, bottom)
        return this
    }

    fun setMargin(left: Int, top: Int, right: Int, bottom: Int): SnackBar {
        (viewer.layoutParams as ViewGroup.MarginLayoutParams).leftMargin = left
        (viewer.layoutParams as ViewGroup.MarginLayoutParams).topMargin = top
        (viewer.layoutParams as ViewGroup.MarginLayoutParams).rightMargin = right
        (viewer.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin = bottom
        return this
    }

    fun setTitle(title: String): SnackBar {
        viewer.findViewById<TextView>(R.id.snack_title).apply {
            visibility = View.VISIBLE
            text = title
            textSize = 14.0f
        }
        return this
    }

    fun setCallBack(buttonName: String? = null, onSeeClicked: () -> Unit): SnackBar {
        val v = viewer.findViewById<TextView>(R.id.snack_button)
        v.visibility = View.VISIBLE
        if (buttonName != null) {
            v.text = buttonName
        }
        v.setOnClickListener {
            onSeeClicked()
        }
        return this
    }
}