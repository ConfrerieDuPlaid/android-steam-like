package com.example.android_steam_like

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.core.text.HtmlCompat.fromHtml
import androidx.recyclerview.widget.RecyclerView
import okhttp3.internal.format
import org.json.JSONArray
import org.json.JSONObject

class game_detail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_detail)
        val list = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.avis)
        list.visibility  = View.GONE
        val appId = intent.getStringExtra("appId")

        findViewById<ProgressBar>(R.id.progress_circular).visibility = View.GONE

        http_request("http://172.20.10.5:3000/game/$appId", this::displayDetail).start()

        setOnClickButton(appId)
    }

    private fun setOnClickButton(appId: String?) {
        val list = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.avis)
        val description = findViewById<TextView>(R.id.description)
        val descriptionButton = findViewById<Button>(R.id.description_button)
        val opinionsButton = findViewById<Button>(R.id.opinions)
        val circularWaiting = findViewById<ProgressBar>(R.id.progress_circular)

        descriptionButton.setOnClickListener {
            opinionsButton.setBackgroundColor(Color.TRANSPARENT)
            descriptionButton.setBackgroundColor(0x5D63DE);
            circularWaiting.visibility = View.GONE;
            list.visibility  = View.GONE
            description.visibility = View.VISIBLE
        }

        opinionsButton.setOnClickListener {
            descriptionButton.setBackgroundColor(Color.TRANSPARENT)
            opinionsButton.setBackgroundColor(0x5D63DE);

            list.visibility  = View.VISIBLE

            description.visibility = View.GONE
            circularWaiting.visibility = View.VISIBLE;
            http_request("http://172.20.10.8:3000/comment/$appId", this::opinions).start()

        }
    }

    private fun opinions(res: String) {
        val circularWaiting = findViewById<ProgressBar>(R.id.progress_circular)
        val jsonRes = JSONArray(res);

        for (i in 0 until jsonRes.length()) {
            this@game_detail.runOnUiThread(java.lang.Runnable {
                try{
                    println(jsonRes[i]);
                    circularWaiting.visibility = View.GONE;

                } catch (e: java.lang.Exception){

                }
            })
        }
    }


    private fun displayDetail(res: String) {
        val jsonRes = JSONObject(res)

        println(jsonRes)
        findViewById<TextView>(R.id.description).text = fromHtml(jsonRes.getString("description"), FROM_HTML_MODE_LEGACY);
    }
}