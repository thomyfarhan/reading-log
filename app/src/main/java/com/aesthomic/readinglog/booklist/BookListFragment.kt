package com.aesthomic.readinglog.booklist


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.database.ReadingLogDatabase
import com.aesthomic.readinglog.databinding.FragmentBookListBinding

class BookListFragment : Fragment() {

    private lateinit var binding: FragmentBookListBinding
    private lateinit var viewModel: BookListViewModel
    private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_book_list, container, false)

        initViewModel()
        initRecyclerView()

        viewModel.navigateToReadBook.observe(this, Observer {
            it?.let {
                this.findNavController().navigate(
                    BookListFragmentDirections
                        .actionBookListDestinationToReadBookDestination(-1L, it)
                )
                viewModel.navigateReadBookDone()
            }
        })

        return binding.root
    }

    private fun initRecyclerView() {
        binding.rvBookList.layoutManager = LinearLayoutManager(requireContext())

        val adapter = BookListAdapter(BookListListener {
            viewModel.onBookClicked(it)
        })
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                view.findNavController().popBackStack(R.id.read_book_destination, false)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onPause() {
        super.onPause()
        callback.isEnabled = false
    }

    override fun onResume() {
        super.onResume()
        callback.isEnabled = true
    }
}
