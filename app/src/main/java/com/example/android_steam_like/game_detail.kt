package com.example.android_steam_like

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.core.text.HtmlCompat.fromHtml
import androidx.recyclerview.widget.RecyclerView
import okhttp3.internal.format
import org.json.JSONArray
import org.json.JSONObject

class game_detail : AppCompatActivity() {
    val list = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.avis);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_detail)
        list.visibility  = View.GONE
        val appId = intent.getStringExtra("appId")


        http_request("http://172.20.10.8:3000/game/$appId", this::displayDetail).start()

        setOnClickButton(appId)
    }

    private fun setOnClickButton(appId: String?) {
        val descriptionButton = findViewById<Button>(R.id.description_button)
        val opinionsButton = findViewById<Button>(R.id.opinions)

        descriptionButton.setOnClickListener {
            opinionsButton.setBackgroundColor(Color.TRANSPARENT)
            descriptionButton.setBackgroundColor(0x5D63DE);
        }

        opinionsButton.setOnClickListener {
            descriptionButton.setBackgroundColor(Color.TRANSPARENT)
            opinionsButton.setBackgroundColor(0x5D63DE);

            list.visibility  = View.VISIBLE

            http_request("http://172.20.10.8:3000/comment/$appId", this::opinions).start()

        }
    }

    private fun opinions(res: String) {
        val jsonRes = JSONArray(res);
        println("jsonRes = $jsonRes")
    }


    private fun displayDetail(res: String) {
        val jsonRes = JSONObject(res)

        println(jsonRes)
        findViewById<TextView>(R.id.description).text = fromHtml(jsonRes.getString("description"), FROM_HTML_MODE_LEGACY);
    }
}