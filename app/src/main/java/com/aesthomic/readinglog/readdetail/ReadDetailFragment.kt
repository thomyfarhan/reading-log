package com.aesthomic.readinglog.readdetail


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
import com.aesthomic.readinglog.databinding.FragmentReadDetailBinding

class ReadDetailFragment : Fragment() {

    private lateinit var binding: FragmentReadDetailBinding
    private lateinit var viewModel: ReadDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_read_detail, container, false)

        initViewModel()

        viewModel.navigateToRead.observe(this, Observer {
            if(it) {
                this.findNavController().navigate(
                    ReadDetailFragmentDirections.
                        actionReadDetailDestinationToReadDestination()
                )
                viewModel.doneNavigateToRead()
            }
        })

        return binding.root
    }

    private fun initViewModel() {
        val application = requireNotNull(this.activity).application
        val database = ReadingLogDatabase.getInstance(application)
        val dbRead = database.readDao
        val dbBook = database.bookDao
        val args = ReadDetailFragmentArgs.fromBundle(requireArguments())

        val viewModelFactory = ReadDetailViewModelFactory(args.readKey, dbRead, dbBook)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ReadDetailViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }


}
