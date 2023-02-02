package com.example.android_steam_like

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android_steam_like.components.ActionBar


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        ActionBar.supportActionbar(supportActionBar, ::println, ::println)
        ActionBar.setActionBarTitle(supportActionBar!!.customView, resources.getString(R.string.home))

//        supportActionBar?.hide()
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, LoginFragment())
//            .commit()
    }
}