package com.example.android_steam_like

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.core.text.HtmlCompat.fromHtml
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_steam_like.entities.Comment
import com.example.android_steam_like.entities.Game
import com.example.android_steam_like.utils.HttpRequest
import com.example.android_steam_like.utils.ServerConfig
import layout.Like
import layout.Wish
import org.json.JSONArray
import org.json.JSONObject

class GameDetail : AppCompatActivity() {

    private val comments: MutableList<Comment> = mutableListOf()
    private val listAdapter: CommentsListAdapter = CommentsListAdapter(comments)
    private var game: Game? = null
    private var wishId: String? = null
    private var likeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.game_detail)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.game_detail_action_bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val list = findViewById<RecyclerView>(R.id.game_comments)
        list.visibility  = View.GONE
        val appId = this.intent.getStringExtra("appId")
        setContent()

        HttpRequest(ServerConfig.baseURL()+ "/game/$appId", this::displayDetail).start()

        findViewById<ProgressBar>(R.id.progress_circular).visibility = View.GONE

        setOnClickButton(appId)

        setGameActionButtons()

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

        this@GameDetail.runOnUiThread {
            for (i in 0 until commentsJson.length()) {
                try {
                        val commentJson = commentsJson.getJSONObject(i)
                        val author = commentJson.getString("author")
                        val score = commentJson.getDouble("score")
                        val content = commentJson.getString("content")
                        val comment = Comment(author, content, score)
                        this.comments.add(comment)
                        listAdapter.notifyItemInserted(comments.size + 1)
                        circularWaiting.visibility = View.GONE
                } catch(e: java.lang.Exception) {
                    println(e.message)
                }
            }
        }
    }


    private fun displayDetail(res: String) {
        this.game = Game.newFromGameData(JSONObject(res))
        findViewById<TextView>(R.id.description).text = fromHtml(this.game!!.description!!, FROM_HTML_MODE_LEGACY)
        findViewById<TextView>(R.id.game_name).text = this.game!!.name
        findViewById<TextView>(R.id.game_editor).text = this.game!!.editors
    }


    private fun setGameActionButtons() {
        val appId = this.intent.getStringExtra("appId")!!
        findViewById<ImageButton>(R.id.action_wish_game).setOnClickListener {
            if (this.wishId != null) {
                Wish.removeFromWishlist(this.wishId!!, this::unsetWish)
            } else {
                Wish.addToWishlist(appId, this::setWish)
            }
        }
        findViewById<ImageButton>(R.id.action_like_game).setOnClickListener {
            if (this.likeId != null) {
                Like.removeFromLikelist(this.likeId!!, this::unsetLike)
            } else {
                Like.addToLikelist(appId, this::setLike)
            }
        }
        HttpRequest(ServerConfig.baseURL() + "/wishlist/63aae4227a5a6755c421d4e7?simplified=1", this::setWishButton).start()
        HttpRequest(ServerConfig.baseURL() + "/likelist/63aae4227a5a6755c421d4e7?simplified=1", this::setLikeButton).start()
    }

    private fun setWishButton (res: String) {
        val wishes = JSONArray(res)
        val appId = this.intent.getStringExtra("appId")

        this@GameDetail.runOnUiThread {
            for (i in 0 until wishes.length()) {
                try{
                    val wish = wishes.getJSONObject(i)
                    val wishedAppid: String = wish.getString("appid")
                    if (wishedAppid == appId) {
                        setWish(wish.toString())
                    }
                } catch (e: java.lang.Exception){
                    println(e.message)
                }
            }
        }
    }

    private fun setWish(res: String) {
        val wish = JSONObject(res)
        this.wishId = wish.getString("_id")
        findViewById<ImageButton>(R.id.action_wish_game).setImageResource(R.drawable.whishlist_full)
    }

    private fun unsetWish(res: String = "") {
        this.wishId = null
        findViewById<ImageButton>(R.id.action_wish_game).setImageResource(R.drawable.whishlist)
    }

    private fun setLike(res: String) {
        val like = JSONObject(res)
        this.likeId = like.getString("_id")
        findViewById<ImageButton>(R.id.action_like_game).setImageResource(R.drawable.like_full)
    }

    private fun unsetLike(res: String = "") {
        this.likeId = null
        findViewById<ImageButton>(R.id.action_like_game).setImageResource(R.drawable.like)
    }

    private fun setLikeButton (res: String) {
        val likes = JSONArray(res)
        val appId = this.intent.getStringExtra("appId")

        this@GameDetail.runOnUiThread {
            for (i in 0 until likes.length()) {
                try{
                    val like = likes.getJSONObject(i)
                    val likedAppid: String = like.getString("appid")
                    if (likedAppid == appId) {
                        setLike(like.toString())
                    }
                } catch (e: java.lang.Exception){
                    println(e.message)
                }
            }
        }
    }
}