package com.example.android_steam_like

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.core.text.HtmlCompat.fromHtml
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.android_steam_like.components.ActionBar
import com.example.android_steam_like.databinding.GameDetailBinding
import com.example.android_steam_like.entities.Comment
import com.example.android_steam_like.entities.GameData
import com.example.android_steam_like.utils.CustomSteamAPI
import com.example.android_steam_like.utils.GenericAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import layout.WishLikeData

class GameDetail : Fragment() {
    private lateinit var binding: GameDetailBinding
    private val comments: MutableList<Comment> = mutableListOf()
    private val listAdapter: CommentsListAdapter = CommentsListAdapter(comments)
    private var wishId: String? = null
    private var likeId: String? = null
    private lateinit var appId: String

    override fun onResume() {
        super.onResume()
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar!!
        ActionBar.setActionBarTitle((activity as AppCompatActivity?)!!.supportActionBar!!.customView, resources.getString(R.string.home))
        ActionBar.supportActionbar(actionBar, this::setHeartListener, this::setStarListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GameDetailBinding.inflate(inflater, container, false)
        setupAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setImages()
        appId = requireArguments().getString("appid", "1237970")
        GlobalScope.launch(Dispatchers.Main) {
            getGameById(appId)
//            withContext(Dispatchers.Main) {
//                setImages(activity)
//            }
//            try{
//                val url = URL("https://cdn.akamai.steamstatic.com/steam/apps/1237970/page_bg_generated_v6b.jpg?t=1668565264")
//
//                println("Coroutine")
//                println(BitmapFactory.decodeStream(url.openStream()))
//
//                val bmp = BitmapFactory.decodeStream(url.openStream())
//                println(bmp)
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(context, "Coucou", Toast.LENGTH_SHORT)
//                    binding.background.setImageBitmap(bmp)
//                }
//            } catch (e: MalformedURLException) {
//                e.printStackTrace()
//            }
        }
        setOnClickButton()
        binding.progressCircular.visibility = View.GONE
        binding.gameComments.commentsList.visibility  = View.GONE
    }
    
    private fun setupAdapter () {
        val linearLayoutManager = LinearLayoutManager(context)
        binding.gameComments.commentsList.layoutManager = linearLayoutManager
        binding.gameComments.commentsList.adapter = listAdapter
    }

    private suspend fun getGameById(appId: String) {
        GenericAPI.call(CustomSteamAPI.NetworkRequest::getGameById, appId, this::displayDetail)
    }

    private fun setImages() {
        Glide.with(binding.background.context).load("https://cdn.akamai.steamstatic.com/steam/apps/1237970/page_bg_generated_v6b.jpg?t=1668565264").into(binding.background)
//        HtmlImage(activity, binding.background, requireArguments().getString("backgroundImage", "https://cdn.akamai.steamstatic.com/steam/apps/1237970/page_bg_generated_v6b.jpg?t=1668565264"))
//        HtmlImage(activity, binding.gameCoverImage, requireArguments().getString("headerImage", "https://cdn.akamai.steamstatic.com/steam/apps/1237970/header.jpg?t=1668565264"))
//        HtmlImage(this@GameDetail, findViewById(R.id.title_card_background), this.intent.getStringExtra("backgroundImage"))
     }

    private fun setOnClickButton() {
        val description = binding.description
        val descriptionButton = binding.descriptionButton
        val commentsButton = binding.commentsButton
        val circularWaiting = binding.progressCircular

        descriptionButton.setOnClickListener {
            descriptionButton.setBackgroundResource(R.drawable.button_rounded_left_full)
            commentsButton.setBackgroundResource(R.drawable.button_rounded_right)
            circularWaiting.visibility = View.GONE
            binding.gameComments.commentsList.visibility  = View.GONE
            description.visibility = View.VISIBLE
        }

        commentsButton.setOnClickListener {
            descriptionButton.setBackgroundResource(R.drawable.button_rounded_left)
            commentsButton.setBackgroundResource(R.drawable.button_rounded_right_full)
            binding.gameComments.commentsList.visibility  = View.VISIBLE
            description.visibility = View.GONE
            if (comments.isEmpty()) {
                circularWaiting.visibility = View.VISIBLE
                getGameCommentById()
            }
        }
    }

    private fun getGameCommentById() {
        GlobalScope.launch(Dispatchers.IO) {
            getGameComments(appId)
        }
    }

    private fun addOpinions(res: List<Comment>) {
        GlobalScope.launch(Dispatchers.IO) {
            for (element in res) {
                withContext(Dispatchers.Main) {
                    comments.add(element)
                    listAdapter.notifyItemInserted(comments.size + 1)
                    binding.progressCircular.visibility = View.GONE
                }
            }
        }
    }

    private fun displayDetail(res: GameData) {
        binding.description.text = fromHtml(res.description!!, FROM_HTML_MODE_LEGACY)
        binding.gameName.text = res.name
        binding.gameEditor.text = res.publishers
            .toString()
            .replace("[", "")
            .replace("]", "")
    }

    private fun setHeartListener () {
        GlobalScope.launch(Dispatchers.Main) {
            getLikeList()
        }
        (activity as AppCompatActivity?)!!.findViewById<ImageButton>(R.id.action_heart).setOnClickListener {
            if (likeId != null) {
                GlobalScope.launch(Dispatchers.Main) {
                    removeFromLikeList(likeId!!)
                }
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    addToLikeList(appId)
                }
            }
        }
    }

    private fun setStarListener () {
        GlobalScope.launch(Dispatchers.Main) {
            getWishList()
        }
        (activity as AppCompatActivity?)!!.findViewById<ImageButton>(R.id.action_star).setOnClickListener {
            if (wishId != null) {
                GlobalScope.launch(Dispatchers.Main) {
                    removeFromWishlist(wishId!!)
                }
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    addToWishList(appId)
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

    private fun setWishButton (res: List<WishLikeData>) {
        GlobalScope.launch(Dispatchers.Main) {
            for (element in res) {
                val wishedAppid: String = element.appid
                if (wishedAppid == appId) {
                    setWish(element)
                }
            }
        }
    }

    private fun setWish(res: WishLikeData) {
        this.wishId = res._id
        (activity as AppCompatActivity?)!!.findViewById<ImageButton>(R.id.action_star).setImageResource(R.drawable.whishlist_full)
    }

    private fun unsetWish(isGood: Int) {
        this.wishId = null
        (activity as AppCompatActivity?)!!.findViewById<ImageButton>(R.id.action_star).setImageResource(R.drawable.whishlist)
    }

    private fun setLike(res: WishLikeData) {
        this.likeId = res._id
        (activity as AppCompatActivity?)!!.findViewById<ImageButton>(R.id.action_heart).setImageResource(R.drawable.like_full)
    }

    private fun unsetLike(isGood: Int) {
        this.likeId = null
        (activity as AppCompatActivity?)!!.findViewById<ImageButton>(R.id.action_heart).setImageResource(R.drawable.like)
    }

    private fun setLikeButton (res: List<WishLikeData>) {
        GlobalScope.launch(Dispatchers.Main) {
            for (element in res) {
                val likedAppid: String = element.appid
                if (likedAppid == appId) {
                    setLike(element)
                }
            }
        }
    }
}