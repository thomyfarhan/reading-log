package com.aesthomic.readinglog.booklist


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.database.ReadingLogDatabase
import com.aesthomic.readinglog.databinding.FragmentBookListBinding

class BookListFragment : Fragment() {

    private lateinit var binding: FragmentBookListBinding
    private lateinit var viewModel: BookListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_book_list, container, false)

        initViewModel()
        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        binding.rvBookList.layoutManager = LinearLayoutManager(requireContext())

        val adapter = BookListAdapter()
        binding.rvBookList.adapter = adapter

        viewModel.books.observe(this, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
    }

    private fun initViewModel() {
        val application = requireNotNull(this.activity).application
        val database = ReadingLogDatabase.getInstance(application)
        val dbBook = database.bookDao

        val viewModelFactory = BookListViewModelFactory(dbBook)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(BookListViewModel::class.java)
    }


}
