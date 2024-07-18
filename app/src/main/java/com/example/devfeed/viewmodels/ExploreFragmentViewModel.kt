package com.example.devfeed.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.devfeed.db.TagsDB
import com.example.devfeed.models.Tag
import com.example.devfeed.repo.Repo
import com.example.devfeed.utils.MyResponse

import kotlinx.coroutines.launch

class ExploreFragmentViewModel(application: Application): AndroidViewModel(application) {
    val tagsListData: MutableLiveData<MyResponse<List<Tag>>> = MutableLiveData()
    val followedTagsListData: MutableLiveData<MyResponse<List<Tag>>> = MutableLiveData()
    val repo by lazy {
        Repo(TagsDB.getDB(application))
    }

    fun getAllTags():MutableLiveData<MyResponse<List<Tag>>>{
        tagsListData.postValue(MyResponse.loading())
        viewModelScope.launch {
            repo.getAllTags().collect({
                tagsListData.postValue(MyResponse.success(data = it))
            })
        }
        return tagsListData
    }

    fun getFollowedTags():MutableLiveData<MyResponse<List<Tag>>>{
        viewModelScope.launch {
            tagsListData.postValue(MyResponse.loading())
            repo.getFollowedTags().collect({
                followedTagsListData.postValue(MyResponse.success(data = it))
            })
        }
        return followedTagsListData
    }

    fun searchTags(query:String):MutableLiveData<MyResponse<List<Tag>>>{
        viewModelScope.launch {
            tagsListData.postValue(MyResponse.loading())
            repo.searchTags(query).collect({
               tagsListData.postValue(MyResponse.success(data = it))
            })
        }
        return tagsListData
    }

    fun followTag(tag: Tag){
        viewModelScope.launch {
            repo.followTag(tag)
        }
    }
}