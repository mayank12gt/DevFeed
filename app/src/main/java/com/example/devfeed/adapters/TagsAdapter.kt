package com.example.devfeed.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.devfeed.R
import com.example.devfeed.models.Tag
import com.example.devfeed.ui.OnFollowClicked
import com.example.devfeed.ui.OnTagClicked

import com.google.android.material.button.MaterialButton

class TagsAdapter(var tagsList: List<Tag>,var onTagClicked: OnTagClicked, var onFollowClicked: OnFollowClicked): RecyclerView.Adapter<TagsAdapter.viewholder>() {

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tagTitle = itemView.findViewById<TextView>(R.id.tag_title)

        var followBtn = itemView.findViewById<MaterialButton>(R.id.follow_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        return viewholder(LayoutInflater.from(parent.context).inflate(R.layout.tag_item,parent,false))
    }

    override fun getItemCount(): Int {
        return tagsList.size
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        val tag = tagsList.get(position)
        holder.tagTitle.text = tag.title


        if(tag.isFollowed){
            holder.followBtn.text = "Followed"

        }
        else{
            holder.followBtn.text = "Follow"
        }

        holder.itemView.setOnClickListener {
            onTagClicked.onTagClicked(tag = tag)
        }
        holder.followBtn.setOnClickListener {
            onFollowClicked.onFollowClicked(tag)
        }
    }

    fun update(tags:List<Tag>){
        this.tagsList = tags
        notifyDataSetChanged()
    }
}