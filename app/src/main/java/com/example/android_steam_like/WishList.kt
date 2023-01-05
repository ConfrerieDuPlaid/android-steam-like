package com.example.android_steam_like

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray

class WishList : AppCompatActivity() {
    private val wishes: MutableList<Game> = mutableListOf()
    private val listAdapter = ListAdapter(wishes)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wishlist)

        val list = findViewById<RecyclerView>(R.id.game_list);
        list.visibility  = View.GONE

        if (wishes.isEmpty())
            HttpRequest(ServerConfig.baseURL()+ "/wishlist/63aae4227a5a6755c421d4e7", this::addGame).start()

        val listAdapter = ListAdapter(wishes);
        findViewById<RecyclerView>(R.id.game_list).apply {
            layoutManager = LinearLayoutManager(this@WishList)
            adapter = listAdapter;
        };
    }

    private fun addGame(res: String){
        val gameJson = JSONArray(res)
        this@WishList.runOnUiThread {
            for (i in 0 until gameJson.length()) {
                try{
                    val game = gameJson.getJSONObject(i).getJSONObject("gameData")
                    val newGame = Game.newFromGameData(game)
                    this.wishes.add(newGame)
                    listAdapter.notifyItemInserted(wishes.size + 1)
                    println(newGame)
                } catch (e: java.lang.Exception){
                    println(e.message)
                }
            }
            if (this.wishes.isNotEmpty()) {
                findViewById<RecyclerView>(R.id.game_list).visibility = View.VISIBLE
                findViewById<TextView>(R.id.empty_wish_text).visibility = View.GONE;
                findViewById<ImageView>(R.id.wish_image).visibility = View.GONE;
            }
        }
    }
}