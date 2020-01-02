package com.aesthomic.readinglog.tab

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.host.BookHostFragment
import com.aesthomic.readinglog.host.ReadHostFragment

class TabMainAdapter(private val context: Context, fm: FragmentManager):
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabTitles = intArrayOf(R.string.tab_main_1, R.string.tab_main_2)

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> ReadHostFragment()
            1 -> BookHostFragment()
            else -> null
        } as Fragment
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(tabTitles[position])
    }
}