package com.example.android_steam_like

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
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
        binding.reload.visibility = View.GONE
        binding.errors.visibility = View.GONE
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
        try {
            GenericAPI.call(CustomSteamAPI.NetworkRequest::getGameTop100, null, this::addGameToList)

        }catch (e: Exception){
            withContext(Dispatchers.Main){
                binding.reload.visibility = View.VISIBLE
                binding.errors.visibility = View.VISIBLE
                binding.errors.text = e.message
                binding.homeProgressBar.visibility = View.GONE
            }
        }
    }

    private fun addGameToList(res: MutableList<GameResponse>){
        lateinit var newGame: Game
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main){
                binding.homeProgressBar.visibility = View.GONE
                binding.reload.visibility = View.GONE
                binding.errors.visibility = View.GONE
            }
            for (element in res) {
                try {
                    newGame = Game.newFromGameData(element.gameData)
                    withContext(Dispatchers.Main) {
                        games.add(newGame)
                        listAdapter.notifyItemInserted(games.size + 1)
                    }
                } catch (e: Exception) {

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

    private fun setButtonNavigation() {
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.toString().trim().isEmpty()){return}
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.reload.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                getGames()
            }
        }

        binding.knowMore.setOnClickListener {
            findNavController().navigate(
                R.id.gameDetail,
                bundleOf("appid" to "1237970")
            )
        }
    }
}