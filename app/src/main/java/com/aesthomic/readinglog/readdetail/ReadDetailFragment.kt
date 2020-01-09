package com.aesthomic.readinglog.readdetail


import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.database.ReadingLogDatabase
import com.aesthomic.readinglog.databinding.FragmentReadDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ReadDetailFragment : Fragment() {

    private lateinit var binding: FragmentReadDetailBinding
    private lateinit var viewModel: ReadDetailViewModel

    private lateinit var callback: OnBackPressedCallback

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

        viewModel.book.observe(this, Observer {
            it?.let {
                binding.clReadDetail.visibility = View.VISIBLE
                binding.pbReadDetail.visibility = View.GONE
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                view.findNavController().popBackStack(R.id.read_destination, false)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_read_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.read_detail_delete -> deleteDetail()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteDetail() {
        val dialog = MaterialAlertDialogBuilder(requireActivity())
        dialog.apply {
            setTitle("Are You Sure?")
            setPositiveButton("Yes") { _, _ ->
                viewModel.deleteRead()
                viewModel.eventNavigateToRead()
            }
            setNegativeButton("No", null)
        }
        dialog.show()
    }

    override fun onPause() {
        super.onPause()
        callback.isEnabled = false
        setMenuVisibility(false)
    }

    override fun onResume() {
        super.onResume()
        callback.isEnabled = true
        setMenuVisibility(true)
    }
}
