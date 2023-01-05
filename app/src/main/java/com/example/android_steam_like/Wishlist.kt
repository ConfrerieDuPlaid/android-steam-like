package com.example.android_steam_like

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Wishlist : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wish_list)

        val list = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.game_list);
        list.visibility  = View.GONE
        val booking: MutableList<Game> = mutableListOf()

        val listAdapter = ListAdapter(booking);
        findViewById<RecyclerView>(R.id.game_list).apply {
            layoutManager = LinearLayoutManager(this@Wishlist)
            adapter = listAdapter;
        };

        if(booking.isNotEmpty()){
            list.visibility = View.VISIBLE
            findViewById<TextView>(R.id.empty_like_text).visibility = View.GONE;
            findViewById<ImageView>(R.id.like_image).visibility = View.GONE;
        }
    }
}