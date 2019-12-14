package com.aesthomic.readinglog.read


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.databinding.FragmentReadBinding

class ReadFragment : Fragment() {

    private lateinit var binding: FragmentReadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_read, container, false)

        binding.rvRead.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRead.adapter = ReadAdapter()

        return binding.root
    }


}
