package com.example.android_steam_like

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar()?.setDisplayShowCustomEnabled(true);
        getSupportActionBar()?.setCustomView(R.layout.custom_action_bar);
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        val booking = listOf(
            generateFakeBooking(),
            generateFakeBooking(),
            generateFakeBooking(),
            generateFakeBooking(),
            generateFakeBooking(),
            generateFakeBooking(),
            generateFakeBooking(),
            generateFakeBooking(),
            generateFakeBooking(),
            generateFakeBooking(),
            generateFakeBooking(),
            generateFakeBooking(),
            generateFakeBooking(),
            generateFakeBooking(),
        )

        val listAdapter = ListAdapter(booking);
        findViewById<RecyclerView>(R.id.game_list).apply {
            layoutManager = LinearLayoutManager(this@MainActivity,)
            adapter = listAdapter;
        };

    }
}