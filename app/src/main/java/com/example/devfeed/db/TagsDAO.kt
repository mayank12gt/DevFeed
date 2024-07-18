package com.example.devfeed.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.devfeed.models.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface TagsDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTag(tag: Tag)

    @Query("select * from tags order by isFollowed DESC ")
    fun getAllTags(): Flow<List<Tag>>

    @Query("select * from Tags where isFollowed = 1")
    fun getFollowedTags(): Flow<List<Tag>>

    @Query("select *,INSTR(LOWER(title), LOWER(:query)) AS position from Tags where  title LIKE '%' || :query || '%'  order by position")
    fun searchTags(query: String?): Flow<List<Tag>>


    @Update
    suspend fun updateTag(tag: Tag)

}