package com.example.android_steam_like

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_steam_like.components.ActionBar
import com.example.android_steam_like.entities.Game
import com.example.android_steam_like.utils.CustomSteamAPI
import com.example.android_steam_like.utils.CustomSteamAPI.NetworkRequest.getLikeList
import com.example.android_steam_like.utils.GenericAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import layout.Like
import org.json.JSONArray

class LikeList : AppCompatActivity() {
    private val likes: MutableList<Game> = mutableListOf()
    private val listAdapter = ListAdapter(likes)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.likelist)
        ActionBar.supportActionbar(supportActionBar, ::println, ::println)
        ActionBar.hideUserActionButtons(supportActionBar!!.customView)
        ActionBar.setActionBarTitle(supportActionBar!!.customView, resources.getString(R.string.likelist))

        val list = findViewById<RecyclerView>(R.id.game_list);
        list.visibility  = View.GONE

        GlobalScope.launch(Dispatchers.Main) {
            getLikeList()
        }

        val listAdapter = ListAdapter(likes);
        findViewById<RecyclerView>(R.id.game_list).apply {
            layoutManager = LinearLayoutManager(this@LikeList)
            adapter = listAdapter;
        };
    }

    private suspend fun getLikeList () {

        GenericAPI.call(CustomSteamAPI.NetworkRequest::getLikeList, 0, this::addGame)
    }

    private fun addGame(res: List<CustomSteamAPI.WishLikeData>) {
        this@LikeList.runOnUiThread {
            for (element in res) {
                val game = element.gameData!!
                val newGame = Game.newFromGameData(game)
                this.likes.add(newGame)
                listAdapter.notifyItemInserted(likes.size + 1)
            }
            if (this.likes.isNotEmpty()) {
                findViewById<RecyclerView>(R.id.game_list).visibility = View.VISIBLE
                findViewById<TextView>(R.id.empty_wish_text).visibility = View.GONE;
                findViewById<ImageView>(R.id.wish_image).visibility = View.GONE;
            }
        }
    }
}