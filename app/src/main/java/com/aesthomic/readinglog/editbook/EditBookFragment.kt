package com.aesthomic.readinglog.editbook


import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.book.BookViewModel
import com.aesthomic.readinglog.databinding.FragmentEditBookBinding
import com.aesthomic.readinglog.util.hideKeyboard
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EditBookFragment : Fragment() {

    private lateinit var binding: FragmentEditBookBinding
    private lateinit var callback: OnBackPressedCallback
    private lateinit var doneItem: MenuItem

    private val viewModel: BookViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_book, container, false)

        viewModel.clearBook()
        viewModel.setBookKey(EditBookFragmentArgs.fromBundle(arguments!!).readKey)

        setHasOptionsMenu(true)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.eventNavigateBook.observe(this, Observer {
            if (it) {
                hideKeyboard(requireContext(), requireView())
                this.findNavController().navigate(
                    EditBookFragmentDirections.actionEditBookDestinationToBookDestination()
                )
                viewModel.onNavigateBookDone()
            }
        })

        viewModel.updateInability.observe(this, Observer {
            if (::doneItem.isInitialized) {
                doneItem.isVisible = it
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                view.findNavController().popBackStack(R.id.book_destination, false)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_edit_book, menu)
        doneItem = menu.findItem(R.id.edit_book_done)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.edit_book_done -> viewModel.onUpdateBook()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        viewModel.checkUpdateField()
    }

    override fun onResume() {
        super.onResume()
        callback.isEnabled = true
        setMenuVisibility(true)
    }

    override fun onPause() {
        super.onPause()
        callback.isEnabled = false
        setMenuVisibility(false)
    }
}
