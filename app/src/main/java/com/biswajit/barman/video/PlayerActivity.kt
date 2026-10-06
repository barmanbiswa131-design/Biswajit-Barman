package com.biswajit.barman.video

import android.app.Activity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.graphics.Color
import android.view.ViewGroup

class PlayerActivity: Activity() {
 override fun onCreate(savedInstanceState: Bundle?){
  super.onCreate(savedInstanceState)
  val web=WebView(this).apply{
   setBackgroundColor(Color.BLACK)
   settings.javaScriptEnabled=true
   settings.domStorageEnabled=true
   settings.mediaPlaybackRequiresUserGesture=false
   webViewClient=WebViewClient()
   loadUrl(intent?.data?.toString() ?: "https://www.youtube.com/")
  }
  setContentView(web,ViewGroup.LayoutParams(-1,-1))
 }
}