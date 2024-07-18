package com.example.devfeed.ui

import com.prof18.rssparser.model.RssItem

interface OnPostItemClicked {
    fun onPostItemClicked(post:RssItem);
}