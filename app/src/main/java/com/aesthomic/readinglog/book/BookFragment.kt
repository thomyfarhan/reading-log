package com.aesthomic.readinglog.book


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager

import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.databinding.FragmentBookBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookFragment : Fragment() {

    private lateinit var binding: FragmentBookBinding
    private val viewModel: BookViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_book, container, false)

        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        binding.rvBook.layoutManager = GridLayoutManager(requireContext(), 2)

        val adapter = BookAdapter()

        binding.rvBook.adapter = adapter

        viewModel.books.observe(this, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
    }


}
