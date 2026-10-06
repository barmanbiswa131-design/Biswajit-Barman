package com.biswajit.barman.video

import android.app.Activity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.graphics.Color
import android.widget.LinearLayout
import android.widget.TextView
import android.view.ViewGroup

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root=LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; setBackgroundColor(Color.BLACK) }
        val title=TextView(this).apply {
            text="BISWATUBE"; textSize=22f; setTextColor(Color.WHITE); setPadding(24,28,24,20)
        }
        val web=WebView(this).apply {
            settings.javaScriptEnabled=true; settings.domStorageEnabled=true
            webViewClient=WebViewClient()
            loadUrl("https://www.youtube.com/")
        }
        root.addView(title, LinearLayout.LayoutParams(-1,80))
        root.addView(web, LinearLayout.LayoutParams(-1,0,1f))
        setContentView(root)
    }
}
