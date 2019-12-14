package com.aesthomic.readinglog.read


import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.database.ReadDao
import com.aesthomic.readinglog.database.ReadingLogDatabase
import com.aesthomic.readinglog.databinding.FragmentReadBinding

class ReadFragment : Fragment() {

    private lateinit var binding: FragmentReadBinding
    private lateinit var application: Application
    private lateinit var database: ReadDao
    private lateinit var viewModelFactory: ReadViewModelFactory
    private lateinit var viewModel: ReadViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_read, container, false)

        initViewModel()

        binding.rvRead.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRead.adapter = ReadAdapter()

        return binding.root
    }

    private fun initViewModel() {
        application = requireNotNull(this.activity).application
        database = ReadingLogDatabase.getInstance(application).readDao

        viewModelFactory = ReadViewModelFactory(database, application)
        viewModel = ViewModelProviders.of(
            this, viewModelFactory).get(ReadViewModel::class.java)
    }


}
