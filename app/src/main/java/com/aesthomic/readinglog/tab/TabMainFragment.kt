package com.aesthomic.readinglog.tab


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.databinding.FragmentTabMainBinding

class TabMainFragment : Fragment() {

    lateinit var binding: FragmentTabMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_tab_main, container, false)

        setupTab()

        return binding.root
    }

    private fun setupTab() {
        val tabMainAdapter = TabMainAdapter(requireContext(), childFragmentManager)
        binding.vpMain.setSwipePagingEnabled(false)
        binding.vpMain.adapter = tabMainAdapter
        binding.tlMain.setupWithViewPager(binding.vpMain)

        (activity as AppCompatActivity).supportActionBar?.elevation = 0f
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val currentItem = binding.vpMain.currentItem
                    if (currentItem != 0) {
                        binding.vpMain.setCurrentItem(0, true)
                    } else {
                        requireActivity().finish()
                    }
                }

            })
    }
}
