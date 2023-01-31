package com.example.android_steam_like

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.core.text.HtmlCompat.fromHtml
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_steam_like.components.ActionBar
import com.example.android_steam_like.entities.Comment
import com.example.android_steam_like.entities.Game
import layout.HtmlImage
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
    private var appId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.appId = this.intent.getStringExtra("appId")

        setContentView(R.layout.game_detail)
        ActionBar.supportActionbar(supportActionBar, this::setHeartListener, this::setStarListener)
        findViewById<TextView>(R.id.view_title).text = "Détails du jeu"

        val list = findViewById<RecyclerView>(R.id.game_comments)
        list.visibility  = View.GONE
        setImages()

        Game.getGameByAppId(appId, this::displayDetail)
        setOnClickButton()

        findViewById<ProgressBar>(R.id.progress_circular).visibility = View.GONE
        findViewById<RecyclerView>(R.id.game_comments).apply {
            layoutManager = LinearLayoutManager(this@GameDetail)
            adapter = listAdapter
        }
    }

    private fun setImages() {
        HtmlImage(this@GameDetail, findViewById(R.id.background), this.intent.getStringExtra("backgroundImage"))
        HtmlImage(this@GameDetail, findViewById(R.id.game_cover_image), this.intent.getStringExtra("headerImage"))
        HtmlImage(this@GameDetail, findViewById(R.id.title_card_background), this.intent.getStringExtra("backgroundImage"))
    }

    private fun setOnClickButton() {
        val list = findViewById<RecyclerView>(R.id.game_comments)
        val description = findViewById<TextView>(R.id.description)
        val descriptionButton = findViewById<Button>(R.id.description_button)
        val commentsButton = findViewById<Button>(R.id.comments_button)
        val circularWaiting = findViewById<ProgressBar>(R.id.progress_circular)

        descriptionButton.setOnClickListener {
            descriptionButton.setBackgroundResource(R.drawable.button_rounded_left_full)
            commentsButton.setBackgroundResource(R.drawable.button_rounded_right)
            circularWaiting.visibility = View.GONE
            list.visibility  = View.GONE
            description.visibility = View.VISIBLE
        }

        commentsButton.setOnClickListener {
            descriptionButton.setBackgroundResource(R.drawable.button_rounded_left)
            commentsButton.setBackgroundResource(R.drawable.button_rounded_right_full)
            list.visibility  = View.VISIBLE
            description.visibility = View.GONE
            if (comments.isEmpty()) {
                circularWaiting.visibility = View.VISIBLE
                Comment.getCommentsByAppId(this.appId, this::addOpinions)
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
                } catch(e: Exception) {
                    println(e.message)
                }
            }
        }
    }


    private fun displayDetail(res: String) {

    }

    private fun setHeartListener () {
        findViewById<ImageButton>(R.id.action_heart).setOnClickListener {
            if (this.likeId != null) {
                Like.removeFromLikelist(this.likeId!!, this::unsetLike)
            } else {
                Like.addToLikelist(this.appId, this::setLike)
            }
        }
        Like.getUserLikelist(this::setLikeButton, true)
    }

    private fun setStarListener () {
        Wish.getUserWishlist(this::setWishButton, true)
        findViewById<ImageButton>(R.id.action_star).setOnClickListener {
            if (this.wishId != null) {
                Wish.removeFromWishlist(this.wishId!!, this::unsetWish)
            } else {
                Wish.addToWishlist(this.appId, this::setWish)
            }
        }
    }

    private fun setWishButton (res: String) {
        val wishes = JSONArray(res)
        this@GameDetail.runOnUiThread {
            for (i in 0 until wishes.length()) {
                try{
                    val wish = wishes.getJSONObject(i)
                    val wishedAppid: String = wish.getString("appid")
                    if (wishedAppid == this.appId) {
                        setWish(wish.toString())
                    }
                } catch (e: Exception){
                    println(e.message)
                }
            }
        }
    }

    private fun setWish(res: String) {
        val wish = JSONObject(res)
        this.wishId = wish.getString("_id")
        findViewById<ImageButton>(R.id.action_star).setImageResource(R.drawable.whishlist_full)
    }

    private fun unsetWish(res: String = "") {
        this.wishId = null
        findViewById<ImageButton>(R.id.action_star).setImageResource(R.drawable.whishlist)
    }

    private fun setLike(res: String) {
        val like = JSONObject(res)
        this.likeId = like.getString("_id")
        findViewById<ImageButton>(R.id.action_heart).setImageResource(R.drawable.like_full)
    }

    private fun unsetLike(res: String = "") {
        this.likeId = null
        findViewById<ImageButton>(R.id.action_heart).setImageResource(R.drawable.like)
    }

    private fun setLikeButton (res: String) {
        val likes = JSONArray(res)
        this@GameDetail.runOnUiThread {
            for (i in 0 until likes.length()) {
                try{
                    val like = likes.getJSONObject(i)
                    val likedAppid: String = like.getString("appid")
                    if (likedAppid == this.appId) {
                        setLike(like.toString())
                    }
                } catch (e: Exception){
                    println(e.message)
                }
            }
        }
    }
}