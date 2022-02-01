package com.example.uitest

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*


class VPAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
    private val items: ArrayList<Fragment> = ArrayList()
    private val itext: ArrayList<String> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return items[position]
    }

    override fun getCount(): Int {
        return items.size
    }

    init {
        //Fragment들을 리스트에 넣어줌
        items.add(InnerCall())
        items.add(OuterCall())

        //탭 이름들을 리스트에 넣어줌
        itext.add("내부 연락망")
        itext.add("외부 연락망")
    }

    //탭에 이름 설정
    override fun getPageTitle(position: Int): CharSequence? {
        return itext.get(position)
    }
}