package layout

import com.example.android_steam_like.utils.HttpRequest
import com.example.android_steam_like.utils.ServerConfig
import org.json.JSONObject

class Wish (
    val id: String,
    val appId: String
) {
    companion object {
        private val wishlistEndpoint = ServerConfig.baseURL() + "/wishlist"

        fun addToWishlist (appId: String?, callback: (res: String) -> Unit) {
            val body = JSONObject(mapOf("user" to "63aae4227a5a6755c421d4e7", "appid" to appId))
            HttpRequest(wishlistEndpoint,
                callback,
                "POST",
                body
            ).start()
        }
    }
}