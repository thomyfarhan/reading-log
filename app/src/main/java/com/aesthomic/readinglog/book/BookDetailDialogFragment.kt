package com.aesthomic.readinglog.book


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment

import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.databinding.FragmentBookDetailDialogBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BookDetailDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentBookDetailDialogBinding
    private val viewModel: BookViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_book_detail_dialog, container, false)

        binding.svBookDetailDesc.setMaxHeight(500)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.setBookKey(BookDetailDialogFragmentArgs.fromBundle(arguments!!).bookKey)

        return binding.root
    }


}
