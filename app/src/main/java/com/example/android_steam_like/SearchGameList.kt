package com.example.android_steam_like

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
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
        displayGamesFromHome(result!!);

        searchGame()

        findViewById<RecyclerView>(R.id.game_list).apply {
            layoutManager = LinearLayoutManager(this@SearchGameList)
            adapter = listAdapter;
        };
    }

    private fun searchGame() {
        val editText = findViewById<EditText>(R.id.search_bar);
        editText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Game.getGamesByName(editText.text.toString(), this::displayGamesFromHome)
                false
            } else {
                false
            }
        }
    }

    private fun displayGamesFromHome(result: String) {
        val resultNumber = findViewById<TextView>(R.id.result_number)
        val jsonResult = JSONArray(result);
        this.games.clear()
        this@SearchGameList.runOnUiThread {
            listAdapter.notifyDataSetChanged()
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
            resultNumber.text = "Nombre de résultats : " + this.games.size.toString()
        }
    }
}