package com.example.android_steam_like

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommentsList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.comments_list)
    }
}

class CommentsListAdapter (private val  comments: List<Comment>): RecyclerView.Adapter<CommentViewHolder>() {

    override fun getItemCount(): Int = comments.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.comment_card, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.updateComment(
            comments[position]
        )
    }


}

class CommentViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    private val username = v.findViewById<TextView>(R.id.comment_username)
    private val content = v.findViewById<TextView>(R.id.comment_content)
    private val stars: List<ImageView> = listOf(
        v.findViewById(R.id.star_1),
        v.findViewById(R.id.star_2),
        v.findViewById(R.id.star_3),
        v.findViewById(R.id.star_4),
        v.findViewById(R.id.star_5)
    )

    fun updateComment(Comment: Comment) {
        username.text = Comment.username
        content.text = Comment.content
        val intScore = ((Comment.score * 100) % 5).toInt()
        println(intScore)
        for (i in 0 until intScore) {
            stars[i].setImageResource(R.drawable.star_full)
        }

    }

}