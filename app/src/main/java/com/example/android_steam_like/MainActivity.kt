package com.example.android_steam_like

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    val booking: MutableList<Game> = mutableListOf()
    val listAdapter = ListAdapter(booking);


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTopBar()

        setButtonNavigation()

        HttpRequest(ServerConfig.baseURL()+ "/game/top100", this::addGame).start()

        findViewById<RecyclerView>(R.id.game_list).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listAdapter;
        };
    }

    private fun setTopBar() {
        getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar()?.setDisplayShowCustomEnabled(true);
        getSupportActionBar()?.setCustomView(R.layout.custom_action_bar);
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
    }

    private fun addGame(res: String){
        val gameJson = JSONArray(res)
        for (i in 0 until gameJson.length()) {
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                try{
                    val game = gameJson.getJSONObject(i).getJSONObject("gameData")
                    val newGame = Game.newFromGameData(game)
                    this.booking.add(newGame)
                    listAdapter.notifyItemInserted(booking.size + 1)
                } catch (e: java.lang.Exception){
                    println(e.message)
                }
            })
        }
    }

    private fun setButtonNavigation() {


        findViewById<EditText>(R.id.search_bar).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(s.toString().trim().isEmpty()){return}
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        findViewById<Button>(R.id.know_more).setOnClickListener {
            intent = Intent(this, GameDetail::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.action_bar_like).setOnClickListener {
            intent = Intent(this, Likelist::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.action_bar_favorite).setOnClickListener {
            intent = Intent(this, Wishlist::class.java)
            startActivity(intent)
        }
    }
}