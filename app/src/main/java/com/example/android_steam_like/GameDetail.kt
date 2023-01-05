package com.example.android_steam_like

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.core.text.HtmlCompat.fromHtml
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.json.JSONArray
import org.json.JSONObject

class GameDetail : AppCompatActivity() {

    val comments: MutableList<Comment> = mutableListOf()
    val listAdapter: CommentsListAdapter = CommentsListAdapter(comments)
    var game: Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.game_detail)

        val list = findViewById<RecyclerView>(R.id.game_comments)
        list.visibility  = View.GONE
        val appId = this.intent.getStringExtra("appId")
        setContent()

        HttpRequest(ServerConfig.baseURL()+ "/game/$appId", this::displayDetail).start()

        findViewById<ProgressBar>(R.id.progress_circular).visibility = View.GONE

        setOnClickButton(appId)

        findViewById<RecyclerView>(R.id.game_comments).apply {
            layoutManager = LinearLayoutManager(this@GameDetail)
            adapter = listAdapter
        }
    }

    private fun setContent() {
        val background = findViewById<ImageView>(R.id.background)
        val header = findViewById<ImageView>(R.id.game_cover_image)
        val titleCard = findViewById<ImageView>(R.id.title_card_background)
        Glide.with(this@GameDetail).load(this.intent.getStringExtra("backgroundImage")).into(background)
        Glide.with(this@GameDetail).load(this.intent.getStringExtra("backgroundImage")).into(titleCard)
        Glide.with(this@GameDetail).load(this.intent.getStringExtra("headerImage")).into(header)

    }

    private fun setOnClickButton(appId: String?) {
        val list = findViewById<RecyclerView>(R.id.game_comments)
        val description = findViewById<TextView>(R.id.description)
        val descriptionButton = findViewById<Button>(R.id.description_button)
        val opinionsButton = findViewById<Button>(R.id.opinions)
        val circularWaiting = findViewById<ProgressBar>(R.id.progress_circular)

        descriptionButton.setOnClickListener {
            opinionsButton.setBackgroundColor(Color.TRANSPARENT)
            descriptionButton.setBackgroundColor(0x5D63DE)
            circularWaiting.visibility = View.GONE
            list.visibility  = View.GONE
            description.visibility = View.VISIBLE
        }

        opinionsButton.setOnClickListener {
            descriptionButton.setBackgroundColor(Color.TRANSPARENT)
            opinionsButton.setBackgroundColor(0x5D63DE)

            list.visibility  = View.VISIBLE

            description.visibility = View.GONE
            if (comments.isEmpty()) {
                circularWaiting.visibility = View.VISIBLE
                HttpRequest(ServerConfig.baseURL()+ "/comment/$appId", this::addOpinions).start()
            }
        }
    }

    private fun addOpinions(res: String) {
        val commentsJson = JSONArray(res)
        val circularWaiting = findViewById<ProgressBar>(R.id.progress_circular)
        for (i in 0 until commentsJson.length()) {
            try {
                this@GameDetail.runOnUiThread {
                    val commentJson = commentsJson.getJSONObject(i)
                    val author = commentJson.getString("author")
                    val score = commentJson.getDouble("score")
                    val content = commentJson.getString("content")
                    val comment = Comment(author, content, score)
                    this.comments.add(comment)
                    listAdapter.notifyItemInserted(comments.size + 1)
                    circularWaiting.visibility = View.GONE
                }
            } catch(e: java.lang.Exception) {
                println(e.message)
            }
        }

    }


    private fun displayDetail(res: String) {
        this.game = Game.newFromGameData(JSONObject(res))
        findViewById<TextView>(R.id.description).text = fromHtml(this.game!!.description!!, FROM_HTML_MODE_LEGACY)
        findViewById<TextView>(R.id.game_name).text = this.game!!.name
        findViewById<TextView>(R.id.game_editor).text = this.game!!.editors
    }
}