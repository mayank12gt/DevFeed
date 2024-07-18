package com.example.devfeed.ui

import com.prof18.rssparser.model.RssItem

interface OnBookmarkBtnClicked {
    fun onBookmarkBtnClicked(post:RssItem)
}