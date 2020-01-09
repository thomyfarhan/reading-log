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
import kotlin.math.abs

class TabMainFragment : Fragment() {

    lateinit var binding: FragmentTabMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_tab_main, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarMain)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        setupTab()

        binding.ablMain.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { _, offset ->

                val currentItem = binding.vpMain.currentItem
                val transition = Fade()
                with(transition) {
                    duration = 600
                    addTarget(binding.tvMainSubtitle)
                }

                if (abs(offset) == binding.ablMain.totalScrollRange) {
                    TransitionManager.beginDelayedTransition(binding.clToolbarMain, transition)
                    binding.tvMainSubtitle.visibility = View.VISIBLE
                    binding.tvMainSubtitle.setText(TabMainAdapter.tabTitles[currentItem])
                } else {
                    binding.tvMainSubtitle.visibility = View.GONE
                }
            }
        )

        return binding.root
    }

    private fun setupTab() {
        val tabMainAdapter = TabMainAdapter(requireContext(), childFragmentManager)
        binding.vpMain.setSwipePagingEnabled(false)
        binding.vpMain.adapter = tabMainAdapter
        binding.tlMain.setupWithViewPager(binding.vpMain)
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
