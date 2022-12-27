package com.example.android_steam_like

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.internal.wait
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar()?.setDisplayShowCustomEnabled(true);
        getSupportActionBar()?.setCustomView(R.layout.custom_action_bar);
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        setButtonNavigation()


        val booking = listOf(
            generateFakeGame(),
            generateFakeGame(),
            generateFakeGame(),
            generateFakeGame(),
            generateFakeGame(),
            generateFakeGame(),
            generateFakeGame(),
            generateFakeGame(),
        )

        val listAdapter = ListAdapter(booking);
        findViewById<RecyclerView>(R.id.game_list).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listAdapter;
        };
    }

    private fun setButtonNavigation() {
        val knowMore = findViewById<Button>(R.id.know_more)
        knowMore.setOnClickListener {
            intent = Intent(this, game_detail::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.action_bar_like).setOnClickListener {
            intent = Intent(this, likes::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.action_bar_favorite).setOnClickListener {
            intent = Intent(this, wish_list::class.java)
            startActivity(intent)
        }
    }
}