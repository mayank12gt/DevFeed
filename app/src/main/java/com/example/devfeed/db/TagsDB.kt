package com.example.devfeed.db

import android.content.Context
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.devfeed.models.Source
import com.example.devfeed.models.Tag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Tag::class, Source::class], version = 1)
abstract class TagsDB: RoomDatabase() {

    public abstract fun getTagsDao():TagsDAO
    public abstract fun getSourcesDao():SourceDAO

    companion object{
        @Volatile
        private var Instance:TagsDB?=null

        public fun getDB(context: Context):TagsDB{
           return Instance?: synchronized(this){
               val instance= Room.databaseBuilder(context, TagsDB::class.java,"Tags_DB")
                   .addCallback(object: RoomDatabase.Callback() {
                       override fun onCreate(db: SupportSQLiteDatabase) {
                           super.onCreate(db)
                           CoroutineScope(Dispatchers.IO).launch {
                               Instance?.getTagsDao()?.addTag(Tag(id = 1, title = "#Android",
                           isFollowed = false))
                               Instance?.getTagsDao()?.addTag(Tag(id = 2, title = "#WebDev",
                                   isFollowed = false))
                               Instance?.getTagsDao()?.addTag(Tag(id = 3, title = "#Javascript",
                                   isFollowed = false))
                               Instance?.getTagsDao()?.addTag(Tag(id = 4, title = "#Kotlin",
                                   isFollowed = false))
                               Instance?.getTagsDao()?.addTag(Tag(id = 5, title = "#Golang",
                                   isFollowed = false))
                               Instance?.getTagsDao()?.addTag(Tag(id = 6, title = "#Java",
                                   isFollowed = false))
                               Instance?.getTagsDao()?.addTag(Tag(id = 7, title = "#Machine Learning",
                                   isFollowed = false))
                               Instance?.getTagsDao()?.addTag(Tag(id = 8, title = "#Artificial Intelligence",
                                   isFollowed = false))
                               Instance?.getSourcesDao()?.addSource(Source(id = 1, tag_id = 1,
                                   name = "Dev.to", url = "https://dev.to/feed/tag/android"))
                               Instance?.getSourcesDao()?.addSource(Source(id = 2, tag_id = 1,
                                   name = "Medium.com", url = "https://medium.com/feed/tag/android-development"))
                               Instance?.getSourcesDao()?.addSource(Source(id = 3, tag_id = 1,
                                   name = "Hashnode.com", url = "https://hashnode.com/n/android/rss"))

                               Instance?.getSourcesDao()?.addSource(Source(id = 4, tag_id = 5,
                                   name = "Dev.to", url = "https://dev.to/feed/tag/go"))
                               Instance?.getSourcesDao()?.addSource(Source(id = 5, tag_id = 5,
                                   name = "Medium.com", url = "https://medium.com/feed/tag/golang"))
                               Instance?.getSourcesDao()?.addSource(Source(id = 6, tag_id = 5,
                                   name = "Hashnode.com", url = "https://hashnode.com/n/go/rss"))
                           }

                       }
           }).build()
               Instance = instance
               instance
           }


        }
    }
}