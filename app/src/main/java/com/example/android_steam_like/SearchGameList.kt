package com.example.android_steam_like

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_steam_like.databinding.SearchGameListBinding
import com.example.android_steam_like.entities.Game
import com.example.android_steam_like.entities.GameResponse
import com.example.android_steam_like.utils.CustomSteamAPI
import com.example.android_steam_like.utils.GenericAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray

class SearchGameList : Fragment() {
    private lateinit var binding: SearchGameListBinding
    private val games: MutableList<Game> = mutableListOf()
    private val listAdapter = ListAdapter(games)
    private var searchStr: String = ""

    override fun onResume() {
        super.onResume()
        setupAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchGameListBinding.inflate(inflater, container, false)
        setupAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchStr = requireArguments().getString("searchStr").toString()

        GlobalScope.launch(Dispatchers.IO) {
            if (searchStr != "") {
                GenericAPI.call(CustomSteamAPI.NetworkRequest::searchGamesByName, searchStr, this@SearchGameList::displayGamesFromHome)
            }
            searchGame()
        }
    }

    private fun setupAdapter(){
        val linearLayoutManager = LinearLayoutManager(context)
        binding.gameList.list.layoutManager = linearLayoutManager
        binding.gameList.list.adapter = listAdapter
    }

    private suspend fun searchGame() {
        val editText = binding.searchBar
        editText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val previousContentSize = games.size
                games.clear()
                listAdapter.notifyItemRangeRemoved(0, previousContentSize);

                GlobalScope.launch(Dispatchers.IO) {
                    GenericAPI.call(CustomSteamAPI.NetworkRequest::searchGamesByName, editText.text.toString(), this@SearchGameList::displayGamesFromHome)
                }
                false
            } else {
                false
            }
        }
    }

    private fun displayGamesFromHome(res: MutableList<GameResponse>) {
        GlobalScope.launch(Dispatchers.Main) {
            for (element in res) {
                val game = Game.newFromGameData(element.gameData)
                games.add(game)
                listAdapter.notifyItemInserted(games.size + 1)
            }
            binding.resultNumber.text = "Nombre de résultats : ${res.size}" }
    }
}