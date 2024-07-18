package com.example.devfeed.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.devfeed.models.Tag
import com.example.devfeed.ui.BookmarksFragment
import com.example.devfeed.ui.ExploreFragment
import com.example.devfeed.ui.FeedFragment


class ViewPagerFragAdapter(frag:Fragment, var followedTags: List<Tag>): FragmentStateAdapter(frag) {

    override fun getItemCount(): Int {
       return followedTags.size
    }

    override fun createFragment(position: Int): Fragment {
        return FeedFragment.newInstance(followedTags.get(position))
        //return BookmarksFragment.newInstance()
    }

    fun update(followedTags: List<Tag>){
        this.followedTags = followedTags
        notifyDataSetChanged()
    }
}