package com.example.android_steam_like.entities

import com.example.android_steam_like.utils.HttpRequest
import com.example.android_steam_like.utils.ServerConfig
import org.json.JSONObject

class Game(
    val name: String,
    val editors: String,
    val price: Double,
    val appId: String,
    val headerImage: String,
    val description: String?,
    val backgroundImage: String?,
    val screenshots: MutableList<String>
) {
    fun displayPrice (): String {
        return if (this.price > 0.0)
            "Prix : ${this.price.toString()} €"
        else "Gratuit"
    }

    companion object {
        private val gameEndpoint: String = ServerConfig.baseURL() + "/game"
        private val top100Endpoint: String = "$gameEndpoint/top100"
        private val searchEndpoint: String = "https://steamcommunity.com/actions/SearchApps"

        fun newFromGameData(data: JSONObject): Game {
            val gameName = data.getString("name")
            val editors = data.getJSONArray("publishers").join(", ").replace("\"", "")
            val price = data.getString("priceInCents").toDouble() / 100.0
            val appId = data.getInt("steamAppid").toString()
            val headerImage = data.getString("headerImage")
            val description = data.getString("description")
            val backgroundImage = data.getString("backgroundImage")
            val screenshotsJson = data.getJSONArray("screenshots")
            val screenshots: MutableList<String> = mutableListOf()
            for (i in 0 until screenshotsJson.length()) {
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

        fun getTop100 (callback: (res: String) -> Unit) {
            HttpRequest(top100Endpoint, callback).start()
        }

        fun getGameByAppId (appId: String?, callback: (res: String) -> Unit) {
            HttpRequest("$gameEndpoint/$appId", callback).start()
        }

        fun getGamesByName (name: String, callback: (res: String) -> Unit) {
            HttpRequest("$searchEndpoint/$name", callback).start()
        }
    }

}
