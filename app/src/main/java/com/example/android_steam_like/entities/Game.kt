package com.example.android_steam_like.entities

import com.example.android_steam_like.utils.HttpRequest
import com.example.android_steam_like.utils.ServerConfig
import com.example.android_steam_like.utils.CustomSteamAPI

class Game(
    val name: String,
    val editors: String,
    val price: Double?,
    val appId: String,
    val headerImage: String,
    val description: String?,
    val backgroundImage: String?,
    val screenshots: MutableList<String>
) {

    fun price (): Double {
        return price ?: 0.0;
    }

    fun displayPrice (): String {
        return if (this.price() > 0.0)
            "Prix : ${this.price()} €"
        else "Gratuit"
    }

    companion object {

        fun newFromGameData(data: CustomSteamAPI.GameData): Game {
            val gameName = data.name
            val editors = data.publishers.joinToString()
            val price = data.priceInCents / 100.0
            val appId = data.steamAppid.toString()
            val headerImage = data.headerImage
            val description = data.description
            val backgroundImage = data.backgroundImage
            val screenshotsJson = data.screenshots
            val screenshots: MutableList<String> = mutableListOf()
            for (i in 0 until screenshotsJson.size) {
                screenshots.add(i, screenshotsJson[i].toString())
            }
            return Game(
                gameName,
                editors,
                price,
                appId,
                headerImage,
                description,
                backgroundImage,
                screenshots
            )
        }
    }

}
