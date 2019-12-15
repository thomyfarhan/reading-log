package com.aesthomic.readinglog.read


import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.database.ReadDao
import com.aesthomic.readinglog.database.ReadingLogDatabase
import com.aesthomic.readinglog.databinding.FragmentReadBinding

class ReadFragment : Fragment() {

    private lateinit var binding: FragmentReadBinding
    private lateinit var viewModel: ReadViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_read, container, false)

        initViewModel()
        initRecyclerView()

        viewModel.navigateToReadBook.observe(this, Observer {
            it?.let {
                this.findNavController().navigate(
                    ReadFragmentDirections.
                        actionReadDestinationToReadBookDestination(it.id)
                )
                viewModel.onNavigateReadBookDone()
            }
        })

        return binding.root
    }

    private fun initRecyclerView() {
        binding.rvRead.layoutManager = LinearLayoutManager(requireContext())

        val adapter = ReadAdapter()
        binding.rvRead.adapter = adapter

        viewModel.reads.observe(this, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
    }

    private fun initViewModel() {
        val application = requireNotNull(this.activity).application
        val database = ReadingLogDatabase.getInstance(application).readDao

        val viewModelFactory = ReadViewModelFactory(database, application)
        viewModel = ViewModelProviders.of(
            this, viewModelFactory).get(ReadViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }


}
