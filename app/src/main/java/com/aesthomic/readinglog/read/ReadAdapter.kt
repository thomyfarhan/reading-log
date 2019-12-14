package com.aesthomic.readinglog.read

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter creates a View Holder and fills it with
 * data for the Recycler View to display
 */
class ReadAdapter: RecyclerView.Adapter<ReadAdapter.ReadViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadViewHolder {

    }

    override fun getItemCount(): Int {

    }

    override fun onBindViewHolder(holder: ReadViewHolder, position: Int) {

    }

    /**
     * Layout for the items to be displayed inside the RecyclerView
     */
    inner class ReadViewHolder(view: View): RecyclerView.ViewHolder(view) {}
}
