package com.example.android_steam_like

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Global
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_steam_like.components.ActionBar
import com.example.android_steam_like.databinding.HomeBinding
import com.example.android_steam_like.entities.Game
import com.example.android_steam_like.entities.GameResponse
import com.example.android_steam_like.utils.CustomSteamAPI
import com.example.android_steam_like.utils.GenericAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Home : Fragment() {
    private lateinit var binding: HomeBinding
    private val games: MutableList<Game> = mutableListOf()
    private val listAdapter = ListAdapter(games);

    override fun onResume() {
        super.onResume()
        ActionBar.supportActionbar((activity as AppCompatActivity?)!!.supportActionBar!!, this::setHeartListener, this::setStarListener)
        ActionBar.setActionBarTitle((activity as AppCompatActivity?)!!.supportActionBar!!.customView, resources.getString(R.string.home))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeBinding.inflate(inflater, container, false)
        setupAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlobalScope.launch(Dispatchers.IO) {
            getGames()
            setSearchListener()
        }
        setButtonNavigation()
    }

    private fun setupAdapter(){
        val linearLayoutManager = LinearLayoutManager(context)
        binding.gameList.list.layoutManager = linearLayoutManager
        binding.gameList.list.adapter = listAdapter
    }

    private suspend fun getGames() {
        GenericAPI.call(CustomSteamAPI.NetworkRequest::getGameTop100, null, this::addGameToList)
    }

    private fun addGameToList(res: MutableList<GameResponse>){
        lateinit var newGame: Game
        GlobalScope.launch(Dispatchers.IO) {
            for (element in res) {
                try {
                    newGame = Game.newFromGameData(element.gameData)
                    withContext(Dispatchers.Main) {
                        games.add(newGame)
                        listAdapter.notifyItemInserted(games.size + 1)
                    }
                } catch (e: Exception) {
                    println("$e / $element")
                }
            }
        }
    }

    private fun setHeartListener() {
        (activity as AppCompatActivity?)!!.findViewById<ImageButton>(R.id.action_heart).setOnClickListener {
            findNavController().navigate(R.id.likeList)
        }
    }

    private fun setStarListener() {
        (activity as AppCompatActivity?)!!.findViewById<ImageButton>(R.id.action_star).setOnClickListener {
            findNavController().navigate(R.id.wishList)
        }
    }

    private fun setSearchListener () {
            binding.searchBar.setOnClickListener {
            findNavController().navigate(R.id.searchGameList, bundleOf(
                "searchStr" to binding.searchBar.text.toString()
            ))
        }
//        editText.setOnEditorActionListener { v, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                GlobalScope.launch(Dispatchers.IO) {
//                    GenericAPI.call(CustomSteamAPI.NetworkRequest::searchGamesByName, editText.text.toString(), this@Home::displayGameByName)
//                }
//                false
//            } else {
//                false
//            }
//        }
    }

    private fun setButtonNavigation() {
        binding.knowMore.setOnClickListener {
            findNavController().navigate(
                R.id.gameDetail,
                bundleOf("appid" to "1237970")
            )
        }
    }
}