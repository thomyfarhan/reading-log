package com.aesthomic.readinglog.editbook


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.book.BookViewModel
import com.aesthomic.readinglog.databinding.FragmentEditBookBinding
import com.aesthomic.readinglog.util.setSrcByUriString
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EditBookFragment : Fragment() {

    private lateinit var binding: FragmentEditBookBinding
    private val viewModel: BookViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_book, container, false)

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

        return binding.root
    }


}
