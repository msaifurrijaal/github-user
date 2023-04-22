package com.msaifurrijaal.submissiongithubuser.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.msaifurrijaal.submissiongithubuser.ui.follow.FollowFragment
import com.msaifurrijaal.submissiongithubuser.ui.follow.FollowFragment.Companion.ARG_SECTION_NUMBER

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    private var username: String? = null

    fun setMyString(user: String) {
        username = user
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(ARG_SECTION_NUMBER, position+1)
            putString("username", username)
        }
        return fragment
    }

}