package com.aesthomic.readinglog.read

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aesthomic.readinglog.convertLongToDate
import com.aesthomic.readinglog.convertLongToDuration
import com.aesthomic.readinglog.convertLongToMonth
import com.aesthomic.readinglog.database.Read
import com.aesthomic.readinglog.databinding.ItemListReadBinding

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
    class ReadViewHolder(private val binding: ItemListReadBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(read: Read) {
            binding.read = read

            /**
             * this call is an optimization that asks data binding
             * to execute any pending bindings
             */
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ReadViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListReadBinding.inflate(
                    layoutInflater, parent, false)

                return ReadViewHolder(binding)
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

class ReadListener(val clickListener: (readKey: Long) -> Unit) {
    fun onClick(read: Read) = clickListener(read.id)
}