package com.example.devfeed.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.devfeed.R
import com.example.devfeed.adapters.ViewPagerFragAdapter
import com.example.devfeed.models.Tag
import com.example.devfeed.viewmodels.ExploreFragmentViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"




class HomeFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var followedFeeds:ArrayList<String>
    private  var followedTags:ArrayList<Tag> = ArrayList()
    lateinit var viewModel:ExploreFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        viewModel = ViewModelProvider(this).get(ExploreFragmentViewModel::class.java)
        followedFeeds = ArrayList()
        followedFeeds.add("Latest")
        followedFeeds.add("Android")
        followedFeeds.add("Web")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(R.layout.fragment_home, container, false)
        val homeTabLayout: TabLayout = v.findViewById(R.id.home_tab_layout)
        val homeViewPager: ViewPager2 = v.findViewById(R.id.home_view_pager)
        val tabsAdapter: ViewPagerFragAdapter = ViewPagerFragAdapter(this,followedTags)

        homeViewPager.adapter = tabsAdapter
        viewModel.getFollowedTags().observe(viewLifecycleOwner,{
            Log.d("followedTags",it.toString())
            it.data?.let { it1 -> followedTags.addAll(it1) }
            tabsAdapter.update(followedTags)
        })

        TabLayoutMediator(homeTabLayout, homeViewPager){tab,position-> tab.text=followedTags.get(position).title}
            .attach()


        return v
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}