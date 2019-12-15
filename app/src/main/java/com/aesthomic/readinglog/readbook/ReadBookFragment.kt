package com.aesthomic.readinglog.readbook


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

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

        return binding.root
    }

    private fun initViewModel() {
        val application = requireNotNull(this.activity).application
        val database = ReadingLogDatabase.getInstance(application).readDao
        val readKey = ReadBookFragmentArgs.fromBundle(requireArguments()).readKey

        val viewModelFactory = ReadBookViewModelFactory(
            readKey, database, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ReadBookViewModel::class.java)
    }


}
