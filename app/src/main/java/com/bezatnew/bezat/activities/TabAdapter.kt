package com.bezatnew.bezat.activities

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class TabAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm){
    private val list = mutableListOf<Fragment>()
    fun addFragment(fragment: Fragment){
        list.add(fragment)
        notifyDataSetChanged()
    }
    override fun getItem(p0: Int): Fragment = list[p0]

    override fun getCount(): Int = list.size
}