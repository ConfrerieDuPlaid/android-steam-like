package com.example.android_steam_like

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_steam_like.components.ActionBar
import com.example.android_steam_like.databinding.WishlistBinding
import com.example.android_steam_like.entities.Game
import com.example.android_steam_like.utils.CustomSteamAPI
import com.example.android_steam_like.utils.GenericAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import layout.WishLikeData

class WishList : Fragment() {
    private lateinit var binding: WishlistBinding
    private lateinit var actionBar: androidx.appcompat.app.ActionBar
    private val wishes: MutableList<Game> = mutableListOf()
    private val listAdapter = ListAdapter(wishes)

    override fun onResume() {
        super.onResume()
        actionBar = (activity as AppCompatActivity?)!!.supportActionBar!!
        ActionBar.setActionBarTitle(actionBar.customView, resources.getString(R.string.wishlist))
        ActionBar.hideUserActionButtons(actionBar.customView)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WishlistBinding.inflate(inflater, container, false)
        setupAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch(Dispatchers.Main) {
            getWishList()
        }
    }

    private fun setupAdapter(){
        val linearLayoutManager = LinearLayoutManager(context)
        binding.gameList.list.layoutManager = linearLayoutManager
        binding.gameList.list.adapter = listAdapter
    }

    private suspend fun getWishList() {
        GenericAPI.call(CustomSteamAPI.NetworkRequest::getWihList, 0, this::addGame)
    }

    private fun addGame(res: List<WishLikeData>){
        GlobalScope.launch(Dispatchers.Main) {
            for (element in res) {
                val game = element.gameData!!
                val newGame = Game.newFromGameData(game)
                wishes.add(newGame)
                listAdapter.notifyItemInserted(wishes.size + 1)
            }
            if (wishes.isNotEmpty()) {
                binding.gameList.list.visibility = View.VISIBLE
                binding.emptyWishText.visibility = View.GONE;
                binding.wishImage.visibility = View.GONE;
            }
        }
    }
}