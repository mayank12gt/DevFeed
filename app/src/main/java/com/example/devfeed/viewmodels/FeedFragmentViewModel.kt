package com.example.devfeed.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.devfeed.db.TagsDB
import com.example.devfeed.models.Tag
import com.example.devfeed.repo.Repo
import com.example.devfeed.utils.MyResponse
import com.prof18.rssparser.RssParserBuilder
import com.prof18.rssparser.model.RssChannel
import com.prof18.rssparser.model.RssItem
import kotlinx.coroutines.launch
import okhttp3.Cache
import okhttp3.OkHttpClient

class FeedFragmentViewModel(application: Application):AndroidViewModel(application) {
    val repo by lazy {
        Repo(TagsDB.getDB(application))
    }

    val feedData:MutableLiveData<MyResponse<List<Pair<RssChannel,RssItem>>>> = MutableLiveData()

    val cacheSize = (5 * 1024 * 1024).toLong()
    val myCache = Cache(application.cacheDir, cacheSize)
    fun getClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
            .cache(myCache)
            .addInterceptor { chain ->
                var request = chain.request()
                request.newBuilder().header("Cache-Control", "public, max-age=" + 10).build()
//                else
//                    request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
                chain.proceed(request)
            }
            .build()

        return client
    }

    fun getPosts(tag: Tag?):MutableLiveData<MyResponse<List<Pair<RssChannel,RssItem>>>>{
        val postsList:MutableList<Pair<RssChannel,RssItem>> = ArrayList()
        val parser = RssParserBuilder(
            callFactory = getClient(),
        ).build()

        viewModelScope.launch {
            feedData.postValue(MyResponse.loading())
            try {
                tag?.let {
                    repo.getSourcesforATag(it).collect({
                        Log.d("sources",it.toString())
                        it.forEach {
                            val rssChannel = parser.getRssChannel(it?.url.toString())
                            rssChannel.items.forEach {
                                Log.d("posts",it.guid.toString())
                                postsList.add(Pair(rssChannel,it))
                            }
                            Log.d("postCount",postsList.size.toString())
                        }
                        postsList.sortedBy { it.second.pubDate }

                        feedData.postValue(MyResponse.success(data = postsList))
                        Log.d("livedata",postsList.size.toString())
                    })

                }

            }
            catch (exception:Exception){
                Log.d("err",exception.localizedMessage)
                Log.d("err",exception.stackTraceToString())
                feedData.postValue(MyResponse.error(exception.localizedMessage))
                Log.d("errorDialog","errorPosted")
            }
        }
        return feedData
    }

//    fun getPosts(tag: Tag?):MutableLiveData<MyResponse<List<Pair<RssChannel,RssItem>>>>{
//        val postsList:MutableList<Pair<RssChannel,RssItem>> = ArrayList()
//        val parser = RssParserBuilder(
//            callFactory = getClient(),
//        ).build()
//
//        viewModelScope.launch {
//            feedData.postValue(MyResponse.loading())
//            try {
//
//
//                        val rssChannel = parser.getRssChannel("https://dev.to/feed/tag/android")
//                        rssChannel.items.forEach {
//                            Log.d("posts",it.guid.toString())
//                            postsList.add(Pair(rssChannel,it))
//                        }
//                        Log.d("postCount",postsList.size.toString())
//
//                        feedData.postValue(MyResponse.success(data = postsList))
//                        Log.d("livedata",postsList.size.toString())
//
//
//
//
//            }
//            catch (exception:Exception){
//                Log.d("err",exception.localizedMessage)
//                Log.d("err",exception.stackTraceToString())
//                feedData.postValue(MyResponse.error(exception.localizedMessage))
//            }
//        }
//        return feedData
//    }
}
