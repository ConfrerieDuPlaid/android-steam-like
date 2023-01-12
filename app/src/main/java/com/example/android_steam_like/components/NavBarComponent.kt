package com.example.android_steam_like.components

import androidx.appcompat.app.ActionBar
import com.example.android_steam_like.R

class NavBarComponent {
    companion object{
        fun supportActionbar(
            supportActionBar: ActionBar?,
            setHeartActionCallback:  () -> Unit,
            setStarActionCallback:  () -> Unit
        ) {
            supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            supportActionBar?.setDisplayShowCustomEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
            supportActionBar?.setCustomView(R.layout.action_bar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavBarActions(setHeartActionCallback, setStarActionCallback)
        }

        private fun setNavBarActions(
            setHeartActionCallback:  () -> Unit,
            setStarActionCallback:  () -> Unit
        ) {
            setHeartActionCallback()
            setStarActionCallback()
        }
    }
}