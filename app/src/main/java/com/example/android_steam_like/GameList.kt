package com.example.android_steam_like

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GameList: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_list)
        val booking = listOf(
            generateFakeBooking(),
            generateFakeBooking()
        )


        findViewById<RecyclerView>(R.id.list).apply {
            layoutManager = LinearLayoutManager(this@GameList)
            adapter = ListAdapter(booking);
        }
        // TODO Changer cette ligne en fonction des layouts
    }
}

fun generateFakeBooking() = Game("nathan", "sarah");


class ListAdapter(private val products: List<Game>) : RecyclerView.Adapter<BookingViewHolder>() {


    override fun getItemCount(): Int = products.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        return BookingViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.game_info_card, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.updateDay(
            products[position]
        )
    }


}

class BookingViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    private val name = v.findViewById<TextView>(R.id.game_name)
    private val editorName = v.findViewById<TextView>(R.id.editor_name)


    fun updateDay(game: Game) {
        name.text = game.name
        editorName.text = game.editorName
    }

}
