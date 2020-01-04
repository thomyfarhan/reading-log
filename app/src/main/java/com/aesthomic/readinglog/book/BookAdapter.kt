package com.aesthomic.readinglog.book

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.database.Book
import com.aesthomic.readinglog.databinding.ItemListBookBinding
import com.bumptech.glide.Glide
import java.io.File

class BookAdapter: ListAdapter<Book, BookAdapter.BookViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }

    class BookViewHolder(private val binding: ItemListBookBinding):
        RecyclerView.ViewHolder(binding. root) {

        fun bind(book: Book) {
            val imgPath = Uri.parse(book.photo).path
            val context = binding.ivBookPhoto.context

            Glide.with(context)
                .load(if (imgPath.isNullOrBlank()) R.drawable.ic_photo else File(imgPath))
                .into(binding.ivBookPhoto)
        }

        companion object {
            fun from(parent: ViewGroup): BookViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListBookBinding.inflate(layoutInflater)

                return BookViewHolder(binding)
            }
        }
    }
}

class BookDiffCallback: DiffUtil.ItemCallback<Book>() {

    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }

}