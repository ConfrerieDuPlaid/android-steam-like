package com.example.android_steam_like

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_steam_like.components.ActionBar
import com.example.android_steam_like.entities.Game
import com.example.android_steam_like.utils.CustomSteamAPI
import com.example.android_steam_like.utils.GenericAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray


class Home : AppCompatActivity() {

    private val games: MutableList<Game> = mutableListOf()
    private val listAdapter = ListAdapter(games);


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        ActionBar.supportActionbar(supportActionBar, this::setHeartListener, this::setStarListener)
        ActionBar.setActionBarTitle(supportActionBar!!.customView, resources.getString(R.string.home))
        GlobalScope.launch(Dispatchers.Main) {
            getGames()
        }

//        findViewById<TextView>(R.id.view_title).text = "Accueil"
        setButtonNavigation()

        findViewById<RecyclerView>(R.id.game_list).apply {
            layoutManager = LinearLayoutManager(this@Home)
            adapter = listAdapter;
        };
    }

    private suspend fun getGames() {
        GenericAPI.call(CustomSteamAPI.NetworkRequest::getGameTop100, null, this::addGameToList)
    }

    private fun addGameToList(res: MutableList<CustomSteamAPI.GameResponse>){
        this@Home.runOnUiThread {
            for (element in res) {
                val newGame = Game.newFromGameData(element.gameData)
                this.games.add(newGame)
                listAdapter.notifyItemInserted(games.size + 1)
            }
        }
    }

    private fun setHeartListener() {
        findViewById<ImageButton>(R.id.action_heart).setOnClickListener {
            intent = Intent(this, LikeList::class.java)
            startActivity(intent)
        }
    }

    private fun setStarListener() {
        findViewById<ImageButton>(R.id.action_star).setOnClickListener {
            intent = Intent(this, WishList::class.java)
            startActivity(intent)
        }
    }

    private fun setButtonNavigation() {
        findViewById<EditText>(R.id.search_bar).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.toString().trim().isEmpty()){return}
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        findViewById<Button>(R.id.know_more).setOnClickListener {
            intent = Intent(this, GameDetail::class.java)
            startActivity(intent)
        }
    }
}