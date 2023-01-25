package com.example.android_steam_like.entities

import com.example.android_steam_like.utils.HttpRequest
import com.example.android_steam_like.utils.ServerConfig
import org.json.JSONObject

class User (
    val id: String,
    val username: String,
    val email: String
        ) {

    companion object {
        private val userEndpoint = ServerConfig.baseURL() + "/user"
        private val loginEndpoint = "$userEndpoint/login"
        private var INSTANCE: User? = null

        fun setInstanceFromJson (res: JSONObject) {
            INSTANCE = User(
                res.getString("id"),
                res.getString("username"),
                res.getString("email")
            )
        }

        fun getInstance (): User? {
            return INSTANCE
        }

        fun login (email: String, password: String, callback: (res: String) -> Unit, error: (code: Int) -> Unit) {
            val body = JSONObject(mapOf(
                "email" to email,
                "password" to password
            ))
            HttpRequest(
                loginEndpoint,
                callback,
                "POST",
                body,
                error
            ).start()
        }

        fun signin (username: String, email: String, password: String, callback: (res: String) -> Unit, error: (code: Int) -> Unit) {
            val body = JSONObject(mapOf(
                "email" to email,
                "username" to username,
                "password" to password
            ))
            HttpRequest(
                userEndpoint,
                callback,
                "POST",
                body,
                error
            ).start()
        }

        fun requestToken (email: String, callback: (res: String) -> Unit, error: (code: Int) -> Unit) {
            val url = "$userEndpoint/recPwd/$email";
            HttpRequest(
                url,
                callback,
                "PATCH",
                JSONObject(),

            ).start()
        }

        fun resetPassword(password: String, token: String, callback: (res: String) -> Unit){
            val url = "$userEndpoint/$token";
            println(url)
            HttpRequest(
                url,
                callback,
                "PATCH",
                JSONObject(mapOf(
                    "password" to password
                )),
                ).start()
        }
    }
}