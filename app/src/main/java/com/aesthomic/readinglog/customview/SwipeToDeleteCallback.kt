package com.aesthomic.readinglog.customview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.aesthomic.readinglog.R

abstract class SwipeToDeleteCallback(context: Context): ItemTouchHelper.Callback() {

    private val background = ColorDrawable()
    private val backgroundColor = ContextCompat.getColor(context, R.color.colorAccent)
    private val clearPaint = Paint()
    private val deleteDrawable = ContextCompat.getDrawable(context, R.drawable.ic_delete)
    private val intrinsicWidth = deleteDrawable?.intrinsicWidth ?: 0
    private val intrinsicHeight = deleteDrawable?.intrinsicHeight ?: 0

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.RIGHT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.height

        val isCancelled = dX == 0F && !isCurrentlyActive

        if (isCancelled) {
            clearCanvas(c, itemView.right + dX, itemView.top.toFloat(),
                itemView.right.toFloat(), itemView.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        background.color = backgroundColor

        // If swipe from right to left
        // background.setBounds(itemView.right + dX.toInt(), itemView.top,
        //    itemView.right, itemView.bottom)
        background.setBounds(itemView.left, itemView.top,
            itemView.left + dX.toInt(), itemView.bottom)
        background.draw(c)

        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val deleteIconMargin = (itemHeight - intrinsicHeight) / 2

        // If swipe from right to left
        // val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
        // val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconLeft = itemView.left + deleteIconMargin
        val deleteIconRight = itemView.left + deleteIconMargin + intrinsicWidth
        val deleteIconBottom = deleteIconTop + intrinsicHeight

        deleteDrawable?.setBounds(deleteIconLeft, deleteIconTop,
            deleteIconRight, deleteIconBottom)
        deleteDrawable?.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(canvas: Canvas, left: Float, top: Float,
                            right: Float, bottom: Float) {
        clearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        canvas.drawRect(left, top, right, bottom, clearPaint)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.5f
    }
}