package com.example.devfeed.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.devfeed.models.Source
import com.example.devfeed.models.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface SourceDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSource(source: Source)

    @Query("select * from source where tag_id = :tagId")
    fun getAllSourcesforATag(tagId:Int): Flow<List<Source>>

    @Update
    suspend fun updateSource(source: Source)
}