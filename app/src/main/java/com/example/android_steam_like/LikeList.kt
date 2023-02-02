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
import kotlinx.coroutines.withContext
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
        binding.errors.visibility = View.GONE
        binding.reload.visibility = View.GONE

        setUpButton()

        GlobalScope.launch(Dispatchers.Main) {
            getLikeList()
        }
    }

    private fun setUpButton() {
        binding.reload.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                binding.reload.visibility = View.GONE
                binding.errors.visibility = View.GONE
                getLikeList()
            }
        }

    }

    private fun setupAdapter(){
        val linearLayoutManager = LinearLayoutManager(context)
        binding.gameList.list.layoutManager = linearLayoutManager
        binding.gameList.list.adapter = listAdapter
    }

    private suspend fun getLikeList () {
        binding.homeProgressBar.visibility = View.VISIBLE
        try{
            GenericAPI.call(CustomSteamAPI.NetworkRequest::getLikeList, 0, this::addGame)
            withContext(Dispatchers.Main){
                if(likes.isNotEmpty()){
                    binding.reload.visibility = View.GONE
                    binding.errors.visibility = View.GONE
                    binding.homeProgressBar.visibility = View.GONE
                    binding.likeImage.visibility = View.GONE
                    binding.emptyLikeText.visibility = View.GONE
                }
            }
        }catch (e: Exception){
            withContext(Dispatchers.Main){
                binding.reload.visibility = View.VISIBLE
                binding.errors.visibility = View.VISIBLE
                binding.likeImage.visibility = View.VISIBLE
                binding.emptyLikeText.visibility = View.VISIBLE
                binding.errors.text = e.message
                binding.homeProgressBar.visibility = View.GONE
            }
        }
    }

    private fun addGame(res: List<WishLikeData>) {
        if(res.isEmpty()){
            binding.homeProgressBar.visibility = View.GONE
        }
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