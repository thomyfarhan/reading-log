package com.aesthomic.readinglog.read

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aesthomic.readinglog.database.ReadBook
import com.aesthomic.readinglog.databinding.ItemListReadBinding
import com.aesthomic.readinglog.databinding.ItemListReadEvenBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

private const val ITEM_VIEW_TYPE_ODD = 0
private const val ITEM_VIEW_TYPE_EVEN = 1

/**
 * Adapter creates a View Holder and fills it with
 * data for the Recycler View to display
 */
class ReadAdapter(private val clickListener: ReadListener):
    ListAdapter<DataItem, RecyclerView.ViewHolder>(ReadDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    /**
     * Add list and group the list with the data item type
     * by adding conditial whether the read object is odd or even
     */
    fun addSubmitList(list: List<ReadBook>) {
        adapterScope.launch {
            val items = list.map{
                if (it.id % 2L == 0L) {
                    DataItem.ReadItemEven(it)
                } else {
                    DataItem.ReadItemOdd(it)
                }
            }

            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    /**
     * Return the view type based on DataItem type
     */
    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is DataItem.ReadItemOdd -> ITEM_VIEW_TYPE_ODD
            is DataItem.ReadItemEven -> ITEM_VIEW_TYPE_EVEN
        }
    }

    /**
     * Create the view by inflating layout
     * and return the view as ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_ODD -> ReadOddViewHolder.from(parent)
            ITEM_VIEW_TYPE_EVEN -> ReadEvenViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    /**
     * Bind the view that we already create on onCreateViewHolder function
     * every items in the list need to run this function
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ReadOddViewHolder -> {
                val readItem = getItem(position) as DataItem.ReadItemOdd
                holder.bind(readItem.readBook, clickListener)
            }
            is ReadEvenViewHolder -> {
                val readItem = getItem(position) as DataItem.ReadItemEven
                holder.bind(readItem.readBook, clickListener)
            }
        }
    }

    /**
     * Layout for the items to be displayed inside the RecyclerView
     */
    class ReadOddViewHolder(private val binding: ItemListReadBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(readBook: ReadBook, clickListener: ReadListener) {
            binding.readBook = readBook
            binding.clickListener = clickListener

            /**
             * this call is an optimization that asks data binding
             * to execute any pending bindings
             */
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ReadOddViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListReadBinding.inflate(
                    layoutInflater, parent, false)

                return ReadOddViewHolder(binding)
            }
        }
    }

    /**
     * The second view holder for even data type
     */
    class ReadEvenViewHolder(private val binding: ItemListReadEvenBinding):
            RecyclerView.ViewHolder(binding.root) {

        fun bind(readBook: ReadBook, clickListener: ReadListener) {
            binding.readBook = readBook
            binding.clickListener = clickListener

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ReadEvenViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListReadEvenBinding.inflate(
                    layoutInflater, parent, false)

                return ReadEvenViewHolder(binding)
            }
        }
    }
}

/**
 * Use DiffUtil to optimize RecyclerView
 * when data has been changed
 */
class ReadDiffCallback: DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}

class ReadListener(val clickListener: (readKey: Long) -> Unit) {
    fun onClick(readBook: ReadBook) = clickListener(readBook.id)
}

/**
 * Data Item contain the type of the item
 */
sealed class DataItem {
    abstract val id: Long
    data class ReadItemOdd(val readBook: ReadBook): DataItem() {
        override val id: Long = readBook.id
    }

    data class ReadItemEven(val readBook: ReadBook): DataItem() {
        override val id: Long = readBook.id
    }
}