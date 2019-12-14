package com.aesthomic.readinglog.read

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter creates a View Holder and fills it with
 * data for the Recycler View to display
 */
class ReadAdapter: RecyclerView.Adapter<ReadAdapter.ReadViewHolder>() {

    /**
     * Layout for the items to be displayed inside the RecyclerView
     */
    inner class ReadViewHolder(view: View): RecyclerView.ViewHolder(view) {}
}
