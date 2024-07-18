package com.example.devfeed.repo

import com.example.devfeed.db.TagsDB
import com.example.devfeed.models.Source
import com.example.devfeed.models.Tag
import kotlinx.coroutines.flow.Flow

class Repo(val db:TagsDB) {

    suspend fun getAllTags(): Flow<List<Tag>> {
        return db.getTagsDao().getAllTags()
    }
    suspend fun getFollowedTags(): Flow<List<Tag>>{
        return db.getTagsDao().getFollowedTags()
    }

    suspend fun searchTags(query:String): Flow<List<Tag>>{
        return db.getTagsDao().searchTags(query)
    }

    suspend fun getSourcesforATag(tag: Tag): Flow<List<Source>>{
        return db.getSourcesDao().getAllSourcesforATag(tag.id)
    }

    suspend fun followTag(tag: Tag){
        db.getTagsDao().updateTag(tag)
    }
}