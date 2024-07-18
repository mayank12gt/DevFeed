package com.example.devfeed.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devfeed.R
import com.example.devfeed.adapters.PostsAdapter
import com.example.devfeed.models.Tag
import com.example.devfeed.utils.MyResponse
import com.example.devfeed.viewmodels.FeedFragmentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.prof18.rssparser.model.RssChannel
import com.prof18.rssparser.model.RssItem

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_TAG = "tag"
//private const val ARG_PARAM2 = "param2"

class FeedFragment : Fragment(),OnPostItemClicked,OnBookmarkBtnClicked {
//    // TODO: Rename and change types of parameters
   private var tag: Tag? = null
//    private var param2: String? = null
    lateinit var postsRV:RecyclerView
    lateinit var postsAdapter:PostsAdapter
    lateinit var viewmodel:FeedFragmentViewModel
    lateinit var loadingIndicator:CircularProgressIndicator
    private var postsList:List<Pair<RssChannel,RssItem>> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
           tag = it.getSerializable(ARG_TAG) as Tag?
//            param2 = it.getString(ARG_PARAM2)
        }
        viewmodel = ViewModelProvider(this).get(FeedFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

     val v:View =  inflater.inflate(R.layout.fragment_feed, container, false)
        loadingIndicator = v.findViewById(R.id.posts_loading_indicator)
        postsRV = v.findViewById(R.id.posts_rv)
        postsAdapter = PostsAdapter(postsList,this,this)
        postsRV.layoutManager = LinearLayoutManager(context)
        postsRV.adapter = postsAdapter
        loadFeedPosts()
    return v
    }

    private fun loadFeedPosts(){
        Log.d("ErrorDialog","function called")

        viewmodel.getPosts(tag).observe(viewLifecycleOwner,{
            Log.d("ErrorDialog","observer called")
            when(it.status){
                MyResponse.Status.LODING -> {
                    Log.d("ErrorDialog","LoadsTATE called")
                    loadingIndicator.visibility = View.VISIBLE
                }
                MyResponse.Status.SUCCESS -> {
                    Log.d("ErrorDialog","SUCCESSsTATE called")
                    //showErrorDialog()
                    postsAdapter.update(it.data!!)
                    loadingIndicator.visibility = View.GONE
                }
                MyResponse.Status.ERROR -> {
                    Log.d("ErrorDialog","errorstate called")
                    showErrorDialog()
                    Log.d("posts","error")
                    loadingIndicator.visibility =View.GONE
                   // Toast.makeText(context,"Error-${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private var errorDialog: androidx.appcompat.app.AlertDialog? = null
    private fun showErrorDialog(){if (errorDialog == null) {
        errorDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("An Error Occurred")
            .setMessage("Please check your internet connectivity or retry")
            .setNeutralButton("Retry") { dialog, which ->
                dialog.dismiss()
                loadFeedPosts()
            }
            .setOnDismissListener {
                // Reset the dialog instance when dismissed
                errorDialog = null
            }
            .create()
    }

        errorDialog?.show()


    }

    companion object {

        @JvmStatic
        fun newInstance(tag: Tag) =
            FeedFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_TAG, tag)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onBookmarkBtnClicked(post: RssItem) {
        TODO("Not yet implemented")
    }

    override fun onPostItemClicked(post: RssItem) {
      activity?.supportFragmentManager?.beginTransaction()
          ?.add(R.id.frame,PostFragment.newInstance(post.link.toString()))?.addToBackStack(null)?.commit()
    }
}