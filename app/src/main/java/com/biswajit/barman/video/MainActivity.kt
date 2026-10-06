package com.biswajit.barman.video

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.widget.*

class MainActivity : Activity() {
 private lateinit var list: LinearLayout
 private lateinit var search: EditText
 override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); buildUi() }
 private fun buildUi() {
  val root=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setBackgroundColor(Color.rgb(10,10,10))}
  val header=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(18,18,18,8)}
  val brand=TextView(this).apply{text="BiswaTube";textSize=27f;typeface=Typeface.DEFAULT_BOLD;setTextColor(Color.WHITE)}
  header.addView(brand)
  search=EditText(this).apply{hint="Search videos on YouTube";setSingleLine(true);setTextColor(Color.WHITE);setHintTextColor(Color.GRAY);setBackgroundColor(Color.rgb(35,35,35));setPadding(18,0,12,0)}
  val row=LinearLayout(this).apply{gravity=Gravity.CENTER_VERTICAL}
  row.addView(search,LinearLayout.LayoutParams(0,54,1f))
  val go=Button(this).apply{text="SEARCH"}; row.addView(go,LinearLayout.LayoutParams(110,54)); header.addView(row,LinearLayout.LayoutParams(-1,62))
  go.setOnClickListener{openSearch()}
  val cats=LinearLayout(this).apply{orientation=LinearLayout.HORIZONTAL}
  listOf("All","Music","Gaming","Movies","Sports","Funny").forEach{c->val b=Button(this).apply{text=c;textSize=12f};cats.addView(b,LinearLayout.LayoutParams(100,52));b.setOnClickListener{search.setText(c);openSearch()}}
  header.addView(HorizontalScrollView(this).apply{addView(cats)})
  root.addView(header,LinearLayout.LayoutParams(-1,185))
  list=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(18,8,18,18)}
  root.addView(ScrollView(this).apply{addView(list)},LinearLayout.LayoutParams(-1,0,1f))
  addWelcome();setContentView(root)
 }
 private fun addWelcome(){list.removeAllViews();val t=TextView(this).apply{text="Watch YouTube videos inside BiswaTube\n\nSearch above to find videos.";textSize=19f;setTextColor(Color.WHITE);setPadding(10,30,10,30)};list.addView(t);addCard("YouTube Home","Open YouTube recommendations","https://www.youtube.com/")}
 private fun openSearch(){val q=search.text.toString().trim();if(q.isEmpty())return;list.removeAllViews();addCard("Search: $q","YouTube search results","https://www.youtube.com/results?search_query="+Uri.encode(q))}
 private fun addCard(title:String,subtitle:String,url:String){val card=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(18,18,18,18);setBackgroundColor(Color.rgb(28,28,28))};val tv=TextView(this).apply{text=title;textSize=20f;typeface=Typeface.DEFAULT_BOLD;setTextColor(Color.WHITE)};val sub=TextView(this).apply{text=subtitle;textSize=14f;setTextColor(Color.LTGRAY);setPadding(0,8,0,12)};val play=Button(this).apply{text="▶  WATCH"};card.addView(tv);card.addView(sub);card.addView(play);play.setOnClickListener{openVideo(url)};val lp=LinearLayout.LayoutParams(-1,-2);lp.setMargins(0,0,0,14);list.addView(card,lp)}
 private fun openVideo(url:String){startActivity(Intent(this,PlayerActivity::class.java).setData(Uri.parse(url)))}
}