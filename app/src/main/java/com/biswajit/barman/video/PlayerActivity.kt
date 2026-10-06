package com.biswajit.barman.video

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient

class PlayerActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val videoId=intent.getStringExtra("video_id") ?: return
        setTitle(intent.getStringExtra("video_title") ?: "YouTube video")
        val web=WebView(this).apply{
            setBackgroundColor(Color.BLACK)
            settings.javaScriptEnabled=true
            settings.domStorageEnabled=true
            settings.mediaPlaybackRequiresUserGesture=false
            webViewClient=WebViewClient()
            val html="""<!doctype html><html><head><meta name="viewport" content="width=device-width,initial-scale=1"><style>html,body{margin:0;width:100%;height:100%;background:#000;overflow:hidden}iframe{border:0;width:100%;height:100%}</style></head><body><iframe src="https://www.youtube.com/embed/$videoId?playsinline=1" allow="accelerometer;autoplay;clipboard-write;encrypted-media;gyroscope;picture-in-picture;web-share" allowfullscreen></iframe></body></html>"""
            loadDataWithBaseURL("https://www.youtube.com/",html,"text/html","UTF-8",null)
        }
        setContentView(web,ViewGroup.LayoutParams(-1,-1))
    }

    companion object {
        fun intent(context:Context,videoId:String,title:String)=Intent(context,PlayerActivity::class.java).putExtra("video_id",videoId).putExtra("video_title",title)
    }
}
