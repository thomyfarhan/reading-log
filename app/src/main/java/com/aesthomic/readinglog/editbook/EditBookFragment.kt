package com.aesthomic.readinglog.editbook


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.book.BookViewModel
import com.aesthomic.readinglog.databinding.FragmentEditBookBinding
import com.aesthomic.readinglog.util.hideKeyboard
import com.aesthomic.readinglog.util.setSrcByUriString
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EditBookFragment : Fragment() {

    private lateinit var binding: FragmentEditBookBinding
    private lateinit var doneItem: MenuItem

    private val viewModel: BookViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_book, container, false)

        setHasOptionsMenu(true)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.setBookKey(EditBookFragmentArgs.fromBundle(arguments!!).readKey)

        viewModel.book.observe(this, Observer {
            it?.let {
                with(binding) {
                    ivEditBookPhoto.setSrcByUriString(it.photo)
                    etEditBookTitle.setText(it.title)
                    etEditBookPage.setText(it.page.toString())
                    etEditBookDesc.setText(it.desc)
                }
            }
        })

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
}
