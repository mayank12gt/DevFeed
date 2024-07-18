package com.example.devfeed.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.devfeed.R
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.prof18.rssparser.model.RssItem


private const val ARG_POST = "post"



class PostFragment : Fragment() {

    private var postLink: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postLink = it.getString(ARG_POST)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v:View =  inflater.inflate(R.layout.fragment_post, container, false)
        val webView:WebView = v.findViewById(R.id.post_web_view)
        val progressIndicator:LinearProgressIndicator = v.findViewById(R.id.post_progress_indicator)


        webView.webViewClient = WebViewClient()
        webView.webChromeClient = object :WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressIndicator.visibility = View.VISIBLE
                progressIndicator.setProgress(newProgress)
                if(newProgress==100){
                   progressIndicator.visibility = View.GONE
                }
            }
        }
        webView.loadUrl(postLink.toString())

        return v
    }

    companion object {

        @JvmStatic
        fun newInstance(link: String) =
            PostFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_POST,link)

                }
            }
    }
}