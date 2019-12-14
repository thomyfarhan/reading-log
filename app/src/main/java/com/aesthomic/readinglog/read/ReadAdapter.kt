package com.aesthomic.readinglog.read

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.database.Read
import kotlinx.android.synthetic.main.item_list_read.view.*

/**
 * Adapter creates a View Holder and fills it with
 * data for the Recycler View to display
 */
class ReadAdapter(val listReads: MutableList<Read>):
    RecyclerView.Adapter<ReadAdapter.ReadViewHolder>() {

    /**
     * Create the view by inflating layout
     * and return the view as ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_read, parent, false)
        return ReadViewHolder(view)
    }

    /**
     * Return the size of list
     */
    override fun getItemCount(): Int {
        return listReads.size
    }

    /**
     * Bind the view that we already create on onCreateViewHolder function
     * every items in the list need to run this function
     */
    override fun onBindViewHolder(holder: ReadViewHolder, position: Int) {
        holder.bind(listReads[position])
    }

    /**
     * Layout for the items to be displayed inside the RecyclerView
     */
    inner class ReadViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

        fun bind(read: Read) {
            view.tvStartTime.text = read.startTimeMillis.toString()
        }
    }
}
