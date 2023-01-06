package com.example.android_steam_like

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray

class LikeList : AppCompatActivity() {
    private val likes: MutableList<Game> = mutableListOf()
    private val listAdapter = ListAdapter(likes)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.likelist)

        val list = findViewById<RecyclerView>(R.id.game_list);
        list.visibility  = View.GONE

        if (likes.isEmpty())
            HttpRequest(ServerConfig.baseURL()+ "/likelist/63aae4227a5a6755c421d4e7", this::addGame).start()

        val listAdapter = ListAdapter(likes);
        findViewById<RecyclerView>(R.id.game_list).apply {
            layoutManager = LinearLayoutManager(this@LikeList)
            adapter = listAdapter;
        };
    }

    private fun addGame(res: String) {
        val gameJson = JSONArray(res)
        this@LikeList.runOnUiThread {
            for (i in 0 until gameJson.length()) {
                try {
                    val game = gameJson.getJSONObject(i).getJSONObject("gameData")
                    val newGame = Game.newFromGameData(game)
                    this.likes.add(newGame)
                    listAdapter.notifyItemInserted(likes.size + 1)
                } catch (e: java.lang.Exception) {
                    println(e.message)
                }
            }
            if (this.likes.isNotEmpty()) {
                findViewById<RecyclerView>(R.id.game_list).visibility = View.VISIBLE
                findViewById<TextView>(R.id.empty_like_text).visibility = View.GONE;
                findViewById<ImageView>(R.id.like_image).visibility = View.GONE;
            }
        }
    }
}