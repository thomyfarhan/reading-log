package com.aesthomic.readinglog.book


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment

import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.databinding.FragmentBookDetailDialogBinding

class BookDetailDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentBookDetailDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_book_detail_dialog, container, false)

        return binding.root
    }


}
