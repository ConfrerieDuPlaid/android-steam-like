package com.example.android_steam_like

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_steam_like.components.ActionBar
import com.example.android_steam_like.databinding.LikelistBinding
import com.example.android_steam_like.entities.Game
import com.example.android_steam_like.utils.CustomSteamAPI
import com.example.android_steam_like.utils.GenericAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import layout.WishLikeData

class LikeList : Fragment() {
    private lateinit var binding: LikelistBinding
    private lateinit var actionBar: androidx.appcompat.app.ActionBar
    private val likes: MutableList<Game> = mutableListOf()
    private val listAdapter = ListAdapter(likes)

    override fun onResume() {
        super.onResume()
        actionBar = (activity as AppCompatActivity?)!!.supportActionBar!!
        ActionBar.setActionBarTitle(actionBar.customView, resources.getString(R.string.likelist))
        ActionBar.hideUserActionButtons(actionBar.customView)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LikelistBinding.inflate(inflater, container, false)
        setupAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch(Dispatchers.Main) {
            getLikeList()
        }
    }

    private fun setupAdapter(){
        val linearLayoutManager = LinearLayoutManager(context)
        binding.gameList.list.layoutManager = linearLayoutManager
        binding.gameList.list.adapter = listAdapter
    }

    private suspend fun getLikeList () {
        GenericAPI.call(CustomSteamAPI.NetworkRequest::getLikeList, 0, this::addGame)
    }

    private fun addGame(res: List<WishLikeData>) {
        GlobalScope.launch(Dispatchers.Main) {
            for (element in res) {
                val game = element.gameData!!
                val newGame = Game.newFromGameData(game)
                likes.add(newGame)
                listAdapter.notifyItemInserted(likes.size + 1)
            }
            if (likes.isNotEmpty()) {
                binding.gameList.list.visibility = View.VISIBLE
                binding.emptyLikeText.visibility = View.GONE;
                binding.emptyLikeText.visibility = View.GONE;
            }
        }
    }
}