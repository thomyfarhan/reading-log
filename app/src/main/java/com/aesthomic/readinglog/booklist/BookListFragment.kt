package com.aesthomic.readinglog.booklist


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.databinding.FragmentBookListBinding

class BookListFragment : Fragment() {

    private lateinit var binding: FragmentBookListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_book_list, container, false)

        return binding.root
    }


}
