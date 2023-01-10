package layout

import com.example.android_steam_like.utils.HttpRequest
import com.example.android_steam_like.utils.ServerConfig
import org.json.JSONObject

class Like (
    val id: String,
    val appid: String
) {
    companion object {
        private val likelistEndpoint = ServerConfig.baseURL() + "/likelist"

        fun addToLikelist (appid: String, callback: (res: String) -> Unit) {
            val body = JSONObject(mapOf("user" to "63aae4227a5a6755c421d4e7", "appid" to appid))
            HttpRequest(likelistEndpoint,
                callback,
                "POST",
                body
            ).start()
        }

        fun removeFromLikelist(id: String, callback: (res: String) -> Unit) {
            HttpRequest(
                "$likelistEndpoint/$id",
                callback,
                "DELETE"
            ).start()
        }
    }
}