package com.example.android_steam_like

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GameList: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.avis)
        // TODO Changer cette ligne en fonction des layouts
    }
}

class ListAdapter(private val products: List<Game>) : RecyclerView.Adapter<GameViewHolder>() {


    override fun getItemCount(): Int = products.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.game_info_card, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.updateDay(
            products[position]
        )
    }


}

class GameViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    private val name = v.findViewById<TextView>(R.id.game_name)
    private val editorName = v.findViewById<TextView>(R.id.editor_name)
    private val gamePrice = v.findViewById<TextView>(R.id.game_price)
    private val gameImage = v.findViewById<ImageView>(R.id.game_image)
    private val gameButton = v.findViewById<Button>(R.id.more_info)


    fun updateDay(game: Game) {
        name.text = game.name
        editorName.text = game.editorName
        gamePrice.text = game.price
        Glide.with(itemView).load(game.headerImage).into(gameImage)
        gameButton.setOnClickListener {
            val intent = Intent(it.context, GameDetail::class.java)
            intent.putExtra("appId",game.appId)
            intent.putExtra("headerImage",game.headerImage)
            intent.putExtra("backgroundImage",game.backgroundImage)
            startActivity(it.context, intent, null)
        }
    }

}
