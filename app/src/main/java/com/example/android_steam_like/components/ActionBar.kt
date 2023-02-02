package com.example.android_steam_like.components

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import com.example.android_steam_like.R

class ActionBar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.action_bar)
    }

    companion object{
        fun supportActionbar(
            supportActionBar: ActionBar?,
            setHeartActionCallback: () -> Unit,
            setStarActionCallback: () -> Unit
        ) {
            supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            supportActionBar?.setDisplayShowCustomEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
            supportActionBar?.setCustomView(R.layout.action_bar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavBarActions(setHeartActionCallback, setStarActionCallback)
        }

        fun setNavBarActions(
            setHeartActionCallback:  () -> Unit,
            setStarActionCallback:  () -> Unit
        ) {
            setHeartActionCallback()
            setStarActionCallback()
        }

        fun setActionBarTitle(v: View, title: String) {
            val titleTxtView = v.findViewById<TextView>(R.id.view_title);
            titleTxtView.setText(title);
        }

        fun hideUserActionButtons (v: View) {
            v.findViewById<AppCompatImageButton>(R.id.action_heart).visibility = View.GONE
            v.findViewById<AppCompatImageButton>(R.id.action_star).visibility = View.GONE
        }
    }
}