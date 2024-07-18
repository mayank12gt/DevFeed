package com.example.devfeed.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Tags")
data class Tag(@PrimaryKey(autoGenerate = true)
               @ColumnInfo("tag_id")
                val id:Int,
                @ColumnInfo(name = "title")
                val title:String,
                @ColumnInfo(name = "isFollowed")
                var isFollowed:Boolean=false) : Serializable
