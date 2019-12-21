package com.aesthomic.readinglog.booklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.database.Book
import kotlinx.android.synthetic.main.item_list_book.view.*

class BookListAdapter:
    ListAdapter<Book,BookListAdapter.BookListViewHolder>(BookListCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookListViewHolder {
        return BookListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: BookListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BookListViewHolder(private val view: View):
        RecyclerView.ViewHolder(view) {

        companion object {
            fun from(parent: ViewGroup): BookListViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.item_list_book, parent, false)

                return BookListViewHolder(view)
            }
        }

        fun bind(book: Book) {
            view.tv_list_book_title.text = book.title
            view.tv_list_book_page.text = book.page.toString()
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