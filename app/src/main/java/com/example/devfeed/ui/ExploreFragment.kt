package com.example.devfeed.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devfeed.R
import com.example.devfeed.adapters.TagsAdapter
import com.example.devfeed.models.Tag
import com.example.devfeed.utils.MyResponse
import com.example.devfeed.viewmodels.ExploreFragmentViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Timer
import java.util.TimerTask


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ExploreFragment : Fragment(), OnFollowClicked, OnTagClicked {

    private var param1: String? = null
    private var param2: String? = null

    private var tags:MutableList<Tag>   = ArrayList()
    private lateinit var viewModel:ExploreFragmentViewModel
    private lateinit var adapter: TagsAdapter
    private lateinit var progressIndicator: CircularProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
//        tags.add(Tag(1,"#Android",false))
//        tags.add(Tag(1,"#Web",false))
//        tags.add(Tag(1,"#GoLang",false))
//        tags.add(Tag(1,"#Java",false))
//        tags.add(Tag(1,"#Kotlin",false))
        viewModel = ViewModelProvider(this).get(ExploreFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v:View= inflater.inflate(R.layout.fragment_explore, container, false)
        val tagsRv = v.findViewById<RecyclerView>(R.id.tags_rv)
        val searchView:SearchView = v.findViewById(R.id.tags_search_view)
        progressIndicator = v.findViewById(R.id.tags_loading_indicator)
         adapter = TagsAdapter(tags,this,this)
        tagsRv.adapter =adapter
        tagsRv.layoutManager = GridLayoutManager(context,2)

        getTags()


        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            var timer: Timer? = null
            override fun onQueryTextChange(newText: String?): Boolean {

                if(newText?.trim()?.isEmpty()!=true){
                    Log.d("query",newText.toString())
                    timer?.cancel()
                    timer  = Timer()
                    timer?.schedule(object:TimerTask(){
                        override fun run() {
                            Log.d("querySearched",newText.toString())
                            CoroutineScope(Dispatchers.Main).launch {
                                searchTags(newText.toString())
                            }

                        }

                    },0)

                }
                else{
                    Log.d("query","empty")
                    getTags()
                }
                return true
            }

        })





    return v;
    }

    fun getTags(){
        viewModel.getAllTags().observe(viewLifecycleOwner,{
            when(it.status){
                MyResponse.Status.LODING ->{
                    Log.d("loading","true")
                    progressIndicator.visibility = View.VISIBLE}
                MyResponse.Status.SUCCESS ->{
                    Log.d("loading","false")
                    progressIndicator.visibility = View.GONE
                    it.data?.let { it1 -> adapter.update(it1) }
                }
                MyResponse.Status.ERROR -> TODO()
            }

        })
    }

    fun searchTags(query:String){
        viewModel.searchTags(query).observe(viewLifecycleOwner,{
            when(it.status){
                MyResponse.Status.LODING -> {
                    Log.d("loading","true")
                    progressIndicator.visibility = View.VISIBLE
                }
                MyResponse.Status.SUCCESS ->{
                    Log.d("loading","false")
                    progressIndicator.visibility = View.GONE
                    it.data?.let { it1 -> adapter.update(it1) }
                }
                MyResponse.Status.ERROR -> TODO()
            }
        })
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ExploreFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onFollowClicked(tag: Tag) {
        tag.isFollowed=!tag.isFollowed
        viewModel.followTag(tag)
    }

    override fun onTagClicked(tag: Tag) {
        parentFragmentManager.beginTransaction().
        replace(R.id.frame,FeedFragment.newInstance(tag)).addToBackStack(null).commit()
    }
}