package com.aesthomic.readinglog.editbook


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.databinding.FragmentEditBookBinding

class EditBookFragment : Fragment() {

    private lateinit var binding: FragmentEditBookBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_book, container, false)

        return binding.root
    }


}
