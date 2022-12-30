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


class MainActivity : AppCompatActivity() {

    val booking: MutableList<Game> = mutableListOf(
        generateFakeGame(),

    )
    val listAdapter = ListAdapter(booking);


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTopBar()

        setButtonNavigation()

        http_request("http://localhost:3000/game/steam/730", this::addGame).start()

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

    fun addGame(game: Game){
        this.booking.add(game)
        listAdapter
        listAdapter.notifyDataSetChanged();
    }

    private fun setButtonNavigation() {


        findViewById<EditText>(R.id.search_bar).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(s.toString().trim().isEmpty()){return}

                //TODO faire les requêtes necessaires pour chercher des jeux
                println(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        findViewById<Button>(R.id.know_more).setOnClickListener {
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