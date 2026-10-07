package com.biswajit.barman.video

import android.net.Uri
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class VideoItem(val id:String,val title:String,val channel:String,val thumbnail:String)

object YouTubeApi {
    const val API_KEY = AIzaSyCQ8VNJfhh09RYpzh1v2b_iWO0HPNcb1Hc

    fun search(query:String,maxResults:Int=10):List<VideoItem> {
        if (API_KEY.startsWith("PUT_")) throw IllegalStateException("YouTube API key is not set")
        val url=Uri.parse("https://www.googleapis.com/youtube/v3/search").buildUpon()
            .appendQueryParameter("part","snippet")
            .appendQueryParameter("type","video")
            .appendQueryParameter("q",query)
            .appendQueryParameter("maxResults",maxResults.toString())
            .appendQueryParameter("key",API_KEY).build().toString()
        val c=URL(url).openConnection() as HttpURLConnection
        c.requestMethod="GET"; c.connectTimeout=12000; c.readTimeout=12000
        try {
            if(c.responseCode !in 200..299) throw IllegalStateException("YouTube API error: HTTP " + c.responseCode)
            val root=JSONObject(c.inputStream.bufferedReader().use{it.readText()})
            val items=root.optJSONArray("items") ?: return emptyList()
            val out=mutableListOf<VideoItem>()
            for(i in 0 until items.length()){
                val item=items.getJSONObject(i); val id=item.getJSONObject("id").optString("videoId")
                val s=item.getJSONObject("snippet"); val t=s.optJSONObject("thumbnails")
                val thumb=t?.optJSONObject("high")?.optString("url") ?: t?.optJSONObject("medium")?.optString("url") ?: t?.optJSONObject("default")?.optString("url") ?: ""
                if(id.isNotBlank()) out += VideoItem(id,s.optString("title","Untitled video"),s.optString("channelTitle","YouTube"),thumb)
            }
            return out
        } finally { c.disconnect() }
    }
}
