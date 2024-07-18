package com.example.devfeed.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.devfeed.R
import com.example.devfeed.ui.OnBookmarkBtnClicked
import com.example.devfeed.ui.OnPostItemClicked
import com.makeramen.roundedimageview.RoundedImageView
import com.prof18.rssparser.model.RssChannel
import com.prof18.rssparser.model.RssItem
import okhttp3.internal.applyConnectionSpec
import java.net.URL

class PostsAdapter(var posts:List<Pair<RssChannel,RssItem>>, var onPostItemClicked:OnPostItemClicked,
                   var onBookmarkClicked:OnBookmarkBtnClicked): RecyclerView.Adapter<PostsAdapter.viewholder>() {

    class viewholder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val postImage: RoundedImageView = itemView.findViewById(R.id.post_Image)
        val postTitle: TextView = itemView.findViewById(R.id.post_Title)
        val postSource: TextView = itemView.findViewById(R.id.post_Source)
        val postPubDate: TextView = itemView.findViewById(R.id.post_Date)
       // val bookmarkBtn: ImageView = itemView.findViewById(R.id.bookmark_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        return viewholder(LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_post_item2,parent,false))
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
       val post:RssItem = posts.get(position).second
        val channel:RssChannel = posts.get(position).first
        holder.postTitle.text = post.title
        holder.postSource.text = channel.title
        //holder.postPubDate.text = post.pubDate
        if(post.image!=null) {

            Glide.with(holder.itemView.context).load(post.image).placeholder(R.drawable.image_placeholder).into(holder.postImage)
        }
        else{
            Log.d("glideSrc","null")


            var logoUrl:String = extractBaseUrl(channel.link)
            Log.d("glideSrc",logoUrl)
            Glide.with(holder.itemView.context).
            load("https://logo.clearbit.com/${logoUrl}?size=480").
            placeholder(R.drawable.image_placeholder).
            into(holder.postImage)
        }
        holder.itemView.setOnClickListener {
            onPostItemClicked.onPostItemClicked(post)
        }
    }

    fun update(posts: List<Pair<RssChannel,RssItem>>){
        this.posts = posts
        notifyDataSetChanged()
    }

    fun extractBaseUrl(webAddress: String?): String {
        val url = URL(webAddress)
        val protocol = url.protocol
        val host = url.host
       // val port = if (url.port != -1) ":${url.port}" else ""
        return host.toString()
    }
}