package com.aesthomic.readinglog.booklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aesthomic.readinglog.database.Book
import com.aesthomic.readinglog.databinding.ItemListBookBinding

class BookListAdapter(private val clickListener: BookListListener):
    ListAdapter<Book,BookListAdapter.BookListViewHolder>(BookListCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookListViewHolder {
        return BookListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: BookListViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class BookListViewHolder(private val binding: ItemListBookBinding):
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): BookListViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemListBookBinding.inflate(inflater, parent, false)

                return BookListViewHolder(binding)
            }
        }

        fun bind(book: Book, clickListener: BookListListener) {
            binding.book = book
            binding.clickListener = clickListener
        }
    }
}

class BookListCallback: DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }

}

class BookListListener (val clickListener: (id: Long) -> Unit) {
    fun onClick(book: Book) = clickListener(book.id)
}
