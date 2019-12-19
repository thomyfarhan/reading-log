package com.aesthomic.readinglog.readbook


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.database.ReadingLogDatabase
import com.aesthomic.readinglog.databinding.FragmentReadBookBinding

class ReadBookFragment : Fragment() {

    private lateinit var binding: FragmentReadBookBinding
    private lateinit var viewModel: ReadBookViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_read_book, container, false)

        initViewModel()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val time = System.currentTimeMillis()

        viewModel.eventSubmit.observe(this, Observer {
            if (it) {
                viewModel.updateRead(time)
                viewModel.onSubmitDone()

                this.findNavController().navigate(
                    ReadBookFragmentDirections.actionReadBookDestinationToReadDestination())
            }
        })

        return binding.root
    }

    private fun initViewModel() {
        val readKey = ReadBookFragmentArgs.fromBundle(requireArguments()).readKey
        val application = requireNotNull(this.activity).application

        val database = ReadingLogDatabase.getInstance(application)
        val dbRead = database.readDao

        val viewModelFactory = ReadBookViewModelFactory(
            readKey, dbRead)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ReadBookViewModel::class.java)
    }


}
