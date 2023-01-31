package com.example.android_steam_like

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.core.text.HtmlCompat.fromHtml
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_steam_like.components.ActionBar
import com.example.android_steam_like.entities.Comment
import com.example.android_steam_like.entities.Game
import com.example.android_steam_like.utils.GenericAPI
import com.example.android_steam_like.utils.CustomSteamAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import layout.HtmlImage
import org.json.JSONArray

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
        ActionBar.setActionBarTitle(supportActionBar!!.customView, resources.getString(R.string.game_detail))

        GlobalScope.launch(Dispatchers.Main) {
            getGameById(appId!!)
        }

        val list = findViewById<RecyclerView>(R.id.game_comments)
        list.visibility  = View.GONE
        setImages()

        setOnClickButton()

        findViewById<ProgressBar>(R.id.progress_circular).visibility = View.GONE
        findViewById<RecyclerView>(R.id.game_comments).apply {
            layoutManager = LinearLayoutManager(this@GameDetail)
            adapter = listAdapter
        }
    }

    private suspend fun getGameById(appId: String) {
        GenericAPI.call(CustomSteamAPI.NetworkRequest::getGameById, appId, this::displayDetail)
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
                getGameCommentById()
            }
        }
    }

    private fun getGameCommentById() {
        GlobalScope.launch(Dispatchers.Main) {
            getGameComments(appId!!)
        }
    }

    private fun addOpinions(res: List<CustomSteamAPI.comment>) {
        val commentsJson = JSONArray(res)
        val circularWaiting = findViewById<ProgressBar>(R.id.progress_circular)

        this@GameDetail.runOnUiThread {
            for (i in 0 until commentsJson.length()) {
                try {
                        val author = res[i].author
                        val score = res[i].score.toDouble()
                        val content = res[i].content
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

    private fun displayDetail(res: CustomSteamAPI.GameData) {
        findViewById<TextView>(R.id.description).text = fromHtml(res.description!!, FROM_HTML_MODE_LEGACY)
        findViewById<TextView>(R.id.game_name).text = res.name
        findViewById<TextView>(R.id.game_editor).text = res.publishers
            .toString()
            .replace("[", "")
            .replace("]", "")
    }

    private fun setHeartListener () {
        GlobalScope.launch(Dispatchers.Main) {
            getLikeList()
        }
        findViewById<ImageButton>(R.id.action_heart).setOnClickListener {
            if (likeId != null) {
                GlobalScope.launch(Dispatchers.Main) {
                    removeFromLikeList(likeId!!)
                }
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    addToLikeList(appId!!)
                }
            }
        }
    }

    private fun setStarListener () {
        GlobalScope.launch(Dispatchers.Main) {
            getWishList()
        }
        findViewById<ImageButton>(R.id.action_star).setOnClickListener {
            if (wishId != null) {
                GlobalScope.launch(Dispatchers.Main) {
                    removeFromWishlist(wishId!!)
                }
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    addToWishList(appId!!)
                }
            }
        }
    }

    private suspend fun getGameComments(appId: String) {
        GenericAPI.call(CustomSteamAPI.NetworkRequest::getGameCommentById, appId, this::addOpinions)
    }

    private suspend fun getWishList() {
        GenericAPI.call(CustomSteamAPI.NetworkRequest::getWihList, 1, this::setWishButton)
    }

    private suspend fun removeFromWishlist(wishId: String) {
        GenericAPI.call(CustomSteamAPI.NetworkRequest::removeFromWishList, wishId, this::unsetWish)
    }

    private suspend fun addToWishList(appId: String) {
        GenericAPI.call(CustomSteamAPI.NetworkRequest::addToWishList, appId, this::setWish)
    }

    private suspend fun getLikeList() {
        GenericAPI.call(CustomSteamAPI.NetworkRequest::getLikeList, 1, this::setLikeButton)
    }

    private suspend fun removeFromLikeList(likeId: String) {
        GenericAPI.call(CustomSteamAPI.NetworkRequest::removeFromLikeList, likeId, this::unsetLike)
    }

    private suspend fun addToLikeList(appId: String) {
        GenericAPI.call(CustomSteamAPI.NetworkRequest::addToLikeList, appId, this::setLike)
    }

    private fun setWishButton (res: List<CustomSteamAPI.WishLikeData>) {
        this@GameDetail.runOnUiThread {
            for (element in res) {
                val wishedAppid: String = element.appid
                if (wishedAppid == this.appId) {
                    setWish(element)
                }
            }
        }
    }

    private fun setWish(res: CustomSteamAPI.WishLikeData) {
        this.wishId = res._id
        findViewById<ImageButton>(R.id.action_star).setImageResource(R.drawable.whishlist_full)
    }

    private fun unsetWish(isGood: Int) {
        this.wishId = null
        findViewById<ImageButton>(R.id.action_star).setImageResource(R.drawable.whishlist)
    }

    private fun setLike(res: CustomSteamAPI.WishLikeData) {
        this.likeId = res._id
        findViewById<ImageButton>(R.id.action_heart).setImageResource(R.drawable.like_full)
    }

    private fun unsetLike(isGood: Int) {
        this.likeId = null
        findViewById<ImageButton>(R.id.action_heart).setImageResource(R.drawable.like)
    }

    private fun setLikeButton (res: List<CustomSteamAPI.WishLikeData>) {
        this@GameDetail.runOnUiThread {
            for (element in res) {
                val likedAppid: String = element.appid
                if (likedAppid == this.appId) {
                    setLike(element)
                }
            }
        }
    }
}