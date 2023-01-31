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
import com.example.android_steam_like.utils.SteamAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WishList : AppCompatActivity() {
    private val wishes: MutableList<Game> = mutableListOf()
    private val listAdapter = ListAdapter(wishes)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wishlist)
        ActionBar.supportActionbar(supportActionBar, ::println, ::println)
        ActionBar.hideUserActionButtons(supportActionBar!!.customView)
        ActionBar.setActionBarTitle(supportActionBar!!.customView, resources.getString(R.string.wishlist))


        val list = findViewById<RecyclerView>(R.id.game_list);
        list.visibility  = View.GONE

        GlobalScope.launch(Dispatchers.Main) {
            getWishList()
        }

        val listAdapter = ListAdapter(wishes);
        findViewById<RecyclerView>(R.id.game_list).apply {
            layoutManager = LinearLayoutManager(this@WishList)
            adapter = listAdapter;
        };
    }

    private suspend fun getWishList() {
        try {
            val request = withContext(Dispatchers.IO) { SteamAPI.NetworkRequest.getWihList() }
            addGame(request)
        } catch (e: Exception) {
            println("Une erreur est survenue ${e.message}")
        }
    }

    private fun addGame(res: List<SteamAPI.WishListData>){
        this@WishList.runOnUiThread {
            for (element in res) {
                try{
                    val game = element.gameData!!
                    val newGame = Game.newFromGameData(game)
                    this.wishes.add(newGame)
                    listAdapter.notifyItemInserted(wishes.size + 1)
                } catch (e: Exception){
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