package com.keylogic.mindi.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val fragmentList = ArrayList<Pair<String, Fragment>>()

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position].second
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    fun getItemName(position: Int): String {
        return fragmentList[position].first
    }

    fun addFragment(name: String, fragment: Fragment?) {
        fragmentList.add(Pair(name, fragment!!))
    }
}