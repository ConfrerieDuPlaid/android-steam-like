package com.example.android_steam_like

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_steam_like.components.ActionBar
import com.example.android_steam_like.entities.Game
import com.example.android_steam_like.entities.User
import org.json.JSONArray


class Home : AppCompatActivity() {

    private val games: MutableList<Game> = mutableListOf()
    private val listAdapter = ListAdapter(games);


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        ActionBar.supportActionbar(supportActionBar, this::setHeartListener, this::setStarListener)
        findViewById<TextView>(R.id.view_title).text = "Accueil"
        setButtonNavigation()

        if (games.isEmpty())
            Game.getTop100(this::addGameToList)

        findViewById<RecyclerView>(R.id.game_list).apply {
            layoutManager = LinearLayoutManager(this@Home)
            adapter = listAdapter;
        };
    }

    private fun addGameToList(res: String){
        val gameJson = JSONArray(res)

        this@Home.runOnUiThread {
            for (i in 0 until gameJson.length()) {
                try{
                    val game = gameJson.getJSONObject(i).getJSONObject("gameData")
                    val newGame = Game.newFromGameData(game)
                    this.games.add(newGame)
                    listAdapter.notifyItemInserted(games.size + 1)
                } catch (e: java.lang.Exception){
                    println(e.message)
                }
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