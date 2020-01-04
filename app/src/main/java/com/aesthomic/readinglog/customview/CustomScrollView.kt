package com.aesthomic.readinglog.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView
import kotlin.math.min

class CustomScrollView(context: Context, attrs: AttributeSet):
    ScrollView(context, attrs) {

    private var maxHeight = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var newHeightMeasureSpec = heightMeasureSpec

        if (maxHeight > 0) {
            val hSize = MeasureSpec.getSize(heightMeasureSpec)
            val hMode = MeasureSpec.getMode(heightMeasureSpec)

            when (hMode) {
                MeasureSpec.AT_MOST -> {
                    newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                        min(hSize, maxHeight), MeasureSpec.AT_MOST)
                }
                MeasureSpec.UNSPECIFIED -> {
                    newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                        maxHeight, MeasureSpec.AT_MOST)
                }
                MeasureSpec.EXACTLY -> {
                    newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                        min(hSize, maxHeight), MeasureSpec.EXACTLY)
                }
            }
        }

        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }

    fun setMaxHeight(maxHeight: Int) {
        this.maxHeight = maxHeight
    }
}