package com.example.devfeed.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(foreignKeys = arrayOf(ForeignKey(entity = Tag::class,
    parentColumns = arrayOf("tag_id"),
    childColumns = arrayOf("tag_id"),
    onDelete = CASCADE
)), tableName = "source")
data class Source(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("source_id")
    val id: Int,
    @ColumnInfo("tag_id")
    val tag_id: Int,
    @ColumnInfo("name")
    val name: String,
    @ColumnInfo("url")
    val url: String,
)
