package com.example.android_steam_like.entities

import com.example.android_steam_like.utils.HttpRequest
import com.example.android_steam_like.utils.ServerConfig
import com.example.android_steam_like.utils.SteamAPI

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
        private val gameEndpoint: String = ServerConfig.baseURL() + "/game"
        private val top100Endpoint: String = "$gameEndpoint/top100"
        private val searchEndpoint: String = "https://steamcommunity.com/actions/SearchApps"

        fun newFromGameData(data: SteamAPI.GameData): Game {
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

        private suspend fun getGameById(appId: String) {

        }

        fun getTop100 (callback: (res: String) -> Unit) {
            HttpRequest(top100Endpoint, callback).start()
        }

        fun getGameByAppId (appId: String?, callback: (res: String) -> Unit) {
            HttpRequest("$gameEndpoint/$appId", callback).start()
        }

        fun getGamesByName (name: String, callback: (res: String) -> Unit) {
            if (name != "")
                HttpRequest("$searchEndpoint/$name", callback).start()
        }
    }

}
