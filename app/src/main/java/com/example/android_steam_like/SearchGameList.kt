package com.example.android_steam_like

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_steam_like.entities.Game
import org.json.JSONArray

class SearchGameList : AppCompatActivity() {
    private val games: MutableList<Game> = mutableListOf()
    private val listAdapter = ListAdapter(games);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_game_list)

        val result = this.intent.getStringExtra("result");
        displayGames(result!!);

        findViewById<RecyclerView>(R.id.game_list).apply {
            layoutManager = LinearLayoutManager(this@SearchGameList)
            adapter = listAdapter;
        };
    }

    private fun displayGames(result: String) {
        val jsonResult = JSONArray(result);
        this@SearchGameList.runOnUiThread {
            for (i in 0 until jsonResult.length()) {
                try {
                    val game = jsonResult.getJSONObject(i)
                    val newGame = Game(
                        game.getString("name"),
                        "",
                        0.0,
                        game.getString("appid"),
                        game.getString("logo"),
                        "",
                        "",
                        mutableListOf()
                    );
                    this.games.add(newGame)
                    listAdapter.notifyItemInserted(games.size + 1)
                } catch (e: java.lang.Exception) {
                    println(e.message)
                }
            }
        }
    }
}