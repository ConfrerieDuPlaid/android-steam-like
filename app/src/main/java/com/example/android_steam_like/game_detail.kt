package com.example.android_steam_like

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.core.text.HtmlCompat.fromHtml
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

class game_detail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_detail)
        val list = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.avis);
        list.visibility  = View.GONE
        val appId = intent.getStringExtra("appId")


        http_request("http://172.20.10.8:3000/game/$appId", this::displayDetail).start()
    }

    private fun displayDetail(res: String) {
        val jsonRes = JSONObject(res)

        println(jsonRes)
        findViewById<TextView>(R.id.description).text = fromHtml(jsonRes.getString("description"), FROM_HTML_MODE_LEGACY);
    }
}