package com.example.android_steam_like.entities

import com.example.android_steam_like.utils.HttpRequest
import com.example.android_steam_like.utils.ServerConfig

class Comment (
    val username: String,
    val content: String,
    val score: Double) {

    companion object {
        private val commentEndpoint = ServerConfig.baseURL() + "/comment"

        fun getCommentsByAppId (appId: String?, callback: (res: String) -> Unit) {
            HttpRequest("$commentEndpoint/$appId", callback).start()
        }
    }
}