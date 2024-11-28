package com.schedulemanagersp.kotlintest.util

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeHendler(val itemMoveListener: OnItemMoveListener):ItemTouchHelper.Callback() {

    interface OnItemMoveListener{
        fun swiped(position: Int)
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFLags = 0
        val swipeFlags = ItemTouchHelper.START
        return makeMovementFlags(dragFLags,swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        itemMoveListener.swiped(viewHolder.adapterPosition)
    }
}