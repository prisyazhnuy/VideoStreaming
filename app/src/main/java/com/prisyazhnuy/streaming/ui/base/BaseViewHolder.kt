package com.prisyazhnuy.streaming.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<in T>(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

    @Suppress("UnusedPrivateMember")
    abstract fun bind(item: T)
}