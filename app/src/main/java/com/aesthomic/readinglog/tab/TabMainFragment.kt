package com.aesthomic.readinglog.tab


import android.os.Bundle
import android.transition.Fade
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.databinding.FragmentTabMainBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import kotlin.math.abs

class TabMainFragment : Fragment() {

    lateinit var binding: FragmentTabMainBinding
    private var isExpanded = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_tab_main, container, false)

        setupActionBar()
        setupTab()

        return binding.root
    }

    private fun setupActionBar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarMain)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.tvMainSubtitle.setText(TabMainAdapter.tabTitles[0])

        binding.ablMain.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { _, offset ->

                val transition = Fade()
                with(transition) {
                    duration = 600
                    addTarget(binding.tvMainSubtitle)
                }

                if (abs(offset) == binding.ablMain.totalScrollRange) {
                    TransitionManager.beginDelayedTransition(binding.clToolbarMain, transition)
                    binding.tvMainSubtitle.visibility = View.VISIBLE
                    isExpanded = false
                } else {
                    binding.tvMainSubtitle.visibility = View.GONE
                    isExpanded = true
                }
            }
        )

        binding.toolbarMain.setOnClickListener {
            if (isExpanded) {
                binding.ablMain.setExpanded(false)
            } else {
                binding.ablMain.setExpanded(true)
            }
        }
    }

    private fun setupTab() {
        val tabMainAdapter = TabMainAdapter(requireContext(), childFragmentManager)
        binding.vpMain.setSwipePagingEnabled(false)
        binding.vpMain.adapter = tabMainAdapter
        binding.tlMain.setupWithViewPager(binding.vpMain)

        binding.tlMain.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab) {
                val subtitle = TabMainAdapter.tabTitles[tab.position]
                binding.tvMainSubtitle.setText(subtitle)
            }

        })
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
