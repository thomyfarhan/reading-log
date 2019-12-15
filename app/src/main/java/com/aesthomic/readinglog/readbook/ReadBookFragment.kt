package com.aesthomic.readinglog.readbook


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.databinding.FragmentReadBookBinding

class ReadBookFragment : Fragment() {

    private lateinit var binding: FragmentReadBookBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_read_book, container, false)

        return binding.root
    }


}
