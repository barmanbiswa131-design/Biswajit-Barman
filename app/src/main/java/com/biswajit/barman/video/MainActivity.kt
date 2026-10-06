package com.biswajit.barman.video

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : Activity() {
    private lateinit var list: LinearLayout
    private lateinit var search: EditText
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); buildUi() }

    private fun buildUi() {
        val root=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setBackgroundColor(Color.rgb(10,10,10))}
        val header=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(18,18,18,8)}
        header.addView(TextView(this).apply{text="BiswaTube";textSize=27f;typeface=Typeface.DEFAULT_BOLD;setTextColor(Color.WHITE)})
        val row=LinearLayout(this).apply{gravity=Gravity.CENTER_VERTICAL}
        search=EditText(this).apply{hint="Search videos";setSingleLine(true);setTextColor(Color.WHITE);setHintTextColor(Color.GRAY);setBackgroundColor(Color.rgb(35,35,35));setPadding(18,0,12,0)}
        row.addView(search,LinearLayout.LayoutParams(0,54,1f))
        val go=Button(this).apply{text="SEARCH"};row.addView(go,LinearLayout.LayoutParams(110,54));header.addView(row,LinearLayout.LayoutParams(-1,62))
        progress=ProgressBar(this).apply{visibility=View.GONE};header.addView(progress,LinearLayout.LayoutParams(-1,8))
        go.setOnClickListener{openSearch()};search.setOnEditorActionListener{_,_,_->openSearch();true}
        val cats=LinearLayout(this).apply{orientation=LinearLayout.HORIZONTAL}
        listOf("Music","Gaming","Movies","Sports","Funny").forEach{category->val b=Button(this).apply{text=category;textSize=12f};cats.addView(b,LinearLayout.LayoutParams(100,52));b.setOnClickListener{search.setText(category);openSearch()}}
        header.addView(HorizontalScrollView(this).apply{addView(cats)})
        root.addView(header,LinearLayout.LayoutParams(-1,195))
        list=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(18,8,18,18)}
        root.addView(ScrollView(this).apply{addView(list)},LinearLayout.LayoutParams(-1,0,1f))
        setContentView(root);showMessage("Search YouTube videos directly inside BiswaTube.")
    }

    private fun showMessage(message:String){list.removeAllViews();list.addView(TextView(this).apply{text=message;textSize=18f;setTextColor(Color.WHITE);setPadding(10,30,10,30)})}

    private fun openSearch(){
        val q=search.text.toString().trim();if(q.isEmpty())return
        progress.visibility=View.VISIBLE;showMessage("Searching YouTube…")
        Thread{
            try{
                val results=YouTubeApi.search(q)
                runOnUiThread{
                    progress.visibility=View.GONE
                    if(results.isEmpty())showMessage("No videos found.") else {list.removeAllViews();results.forEach{addVideoCard(it)}}
                }
            }catch(e:Exception){
                runOnUiThread{
                    progress.visibility=View.GONE
                    showMessage(if(e.message=="YouTube API key is not set") "YouTube API key is not set yet.\n\nOpen YouTubeApi.kt and replace PUT_YOUR_YOUTUBE_API_KEY_HERE with your own key." else "Search failed. Check internet, API key, and YouTube API quota.\n\n"+(e.message?:"Unknown error"))
                }
            }
        }.start()
    }

    private fun addVideoCard(video:VideoItem){
        val card=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(14,14,14,14);setBackgroundColor(Color.rgb(28,28,28))}
        val image=ImageView(this).apply{scaleType=ImageView.ScaleType.CENTER_CROP;setBackgroundColor(Color.rgb(45,45,45))}
        card.addView(image,LinearLayout.LayoutParams(-1,190));loadThumbnail(image,video.thumbnail)
        card.addView(TextView(this).apply{text=video.title;textSize=17f;typeface=Typeface.DEFAULT_BOLD;setTextColor(Color.WHITE);setPadding(0,10,0,4)})
        card.addView(TextView(this).apply{text=video.channel;textSize=13f;setTextColor(Color.LTGRAY);setPadding(0,0,0,8)})
        val play=Button(this).apply{text="▶ WATCH"};card.addView(play)
        val open=View.OnClickListener{startActivity(PlayerActivity.intent(this@MainActivity,video.id,video.title))}
        card.setOnClickListener(open);play.setOnClickListener(open)
        val lp=LinearLayout.LayoutParams(-1,-2);lp.setMargins(0,0,0,14);list.addView(card,lp)
    }

    private fun loadThumbnail(image:ImageView,url:String){
        if(url.isBlank())return
        Thread{
            try{
                val c=URL(url).openConnection() as HttpURLConnection;c.connectTimeout=8000;c.readTimeout=8000
                val bitmap=c.inputStream.use{android.graphics.BitmapFactory.decodeStream(it)};c.disconnect()
                if(bitmap!=null)runOnUiThread{image.setImageBitmap(bitmap)}
            }catch(_:Exception){}
        }.start()
    }
}
