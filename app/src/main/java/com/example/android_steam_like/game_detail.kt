package com.example.android_steam_like

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.core.text.HtmlCompat.fromHtml
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject

class game_detail : AppCompatActivity() {

    val comments: MutableList<Comment> = mutableListOf()
    val listAdapter: CommentsListAdapter = CommentsListAdapter(comments)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_game_detail)

        val list = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.game_comments)
        list.visibility  = View.GONE
        val appId = intent.getStringExtra("appId")


        http_request(ServerConfig.baseURL()+ "/game/$appId", this::displayDetail).start()

        setOnClickButton(appId)

        findViewById<RecyclerView>(R.id.game_comments).apply {
            layoutManager = LinearLayoutManager(this@game_detail)
            adapter = listAdapter;
        };
    }

    private fun setOnClickButton(appId: String?) {
        val list = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.game_comments)

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

            http_request(ServerConfig.baseURL()+ "/comment/$appId", this::addOpinions).start()

        }
    }

    private fun addOpinions(res: String) {
        val commentsJson = JSONArray(res)
        for (i in 0 until commentsJson.length()) {
            try {
                this@game_detail.runOnUiThread {
                    val commentJson = commentsJson.getJSONObject(i)
                    val author = commentJson.getString("author")
                    val score = commentJson.getDouble("score")
                    val content = commentJson.getString("content")
                    val comment = Comment(author, content, score)
                    this.comments.add(comment)
                    listAdapter.notifyItemInserted(comments.size + 1)
                }
            } catch(e: java.lang.Exception) { }
        }
    }


    private fun displayDetail(res: String) {
        val jsonRes = JSONObject(res)

        println(jsonRes)
        findViewById<TextView>(R.id.description).text = fromHtml(jsonRes.getString("description"), FROM_HTML_MODE_LEGACY);
    }
}