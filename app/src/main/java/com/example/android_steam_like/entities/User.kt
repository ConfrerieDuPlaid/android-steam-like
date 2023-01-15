package com.example.android_steam_like.entities

import com.example.android_steam_like.utils.HttpRequest
import com.example.android_steam_like.utils.ServerConfig
import org.json.JSONObject

class User (
    val id: String,
    val username: String
        ) {

    companion object {
        private val userEndpoint = ServerConfig.baseURL() + "/user"
        private val loginEndpoint = "$userEndpoint/login"

        fun login (email: String, password: String, callback: (res: String) -> Unit, error: (code: Int) -> Unit) {
            val body = JSONObject(mapOf("email" to email, "password" to password))
            HttpRequest(
                loginEndpoint,
                callback,
                "POST",
                body,
                error
            ).start()
        }
    }
}