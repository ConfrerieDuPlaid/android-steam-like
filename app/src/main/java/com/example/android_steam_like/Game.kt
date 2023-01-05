package com.example.android_steam_like

import org.json.JSONObject

class Game(
    val name: String,
    val editors: String,
    val price: String,
    val appId: String,
    val headerImage: String,
    val description: String?,
    val backgroundImage: String?,
    val screenshots: MutableList<String>
) {
    companion object {
        fun newFromGameData(data: JSONObject): Game {
            val gameName = data.getString("name")
            val editors = data.getJSONArray("publishers").join(", ").replace("\"", "")
            val price = data.getString("priceInCents")
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
    }

}
