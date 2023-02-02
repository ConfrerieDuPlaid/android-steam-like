package com.example.android_steam_like

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
                    println(e)
                    print(element)
                }
            }
        }
    }

    private fun setHeartListener() {
        (activity as AppCompatActivity?)!!.findViewById<ImageButton>(R.id.action_heart).setOnClickListener {
            println("Heart")
//            intent = Intent(this, LikeList::class.java)
//            startActivity(intent)
        }
    }

    private fun setStarListener() {
//        findViewById<ImageButton>(R.id.action_star).setOnClickListener {
//            intent = Intent(this, WishList::class.java)
//            startActivity(intent)
//        }
    }

    private fun setButtonNavigation() {
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.toString().trim().isEmpty()){return}
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.knowMore.setOnClickListener {
//            findNavController().navigate(
//                //titan fall 1237970
//            )
//            intent = Intent(this, GameDetail::class.java)
//            startActivity(intent)
        }
    }
}