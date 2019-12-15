package com.aesthomic.readinglog.read

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.convertLongToDate
import com.aesthomic.readinglog.convertLongToDuration
import com.aesthomic.readinglog.convertLongToMonth
import com.aesthomic.readinglog.database.Read
import kotlinx.android.synthetic.main.item_list_read.view.*

/**
 * Adapter creates a View Holder and fills it with
 * data for the Recycler View to display
 */
class ReadAdapter:
    ListAdapter<Read, ReadAdapter.ReadViewHolder>(ReadDiffCallback()) {

    /**
     * Create the view by inflating layout
     * and return the view as ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadViewHolder {
        return ReadViewHolder.from(parent)
    }

    /**
     * Bind the view that we already create on onCreateViewHolder function
     * every items in the list need to run this function
     */
    override fun onBindViewHolder(holder: ReadViewHolder, position: Int) {
        /**
         * same as getItemCount function
         */
        val read = getItem(position)

        holder.bind(read)
    }

    /**
     * Layout for the items to be displayed inside the RecyclerView
     */
    class ReadViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

        fun bind(read: Read) {
            val res = view.context.resources
            view.tv_list_read_month.text =
                convertLongToMonth(read.startTimeMillis)
            view.tv_list_read_date.text =
                convertLongToDate(read.startTimeMillis)
            view.tv_list_read_time.text =
                convertLongToDuration(read.startTimeMillis, read.endTimeMillis, res)
            view.tv_list_read_title.text = read.bookName
        }

        companion object {
            fun from(parent: ViewGroup): ReadViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_read, parent, false)
                return ReadViewHolder(view)
            }
        }
    }
}

/**
 * Use DiffUtil to optimize RecyclerView
 * when data has been changed
 */
class ReadDiffCallback: DiffUtil.ItemCallback<Read>() {
    override fun areItemsTheSame(oldItem: Read, newItem: Read): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Read, newItem: Read): Boolean {
        return oldItem == newItem
    }

}
