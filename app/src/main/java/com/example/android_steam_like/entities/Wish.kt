package layout

import com.example.android_steam_like.utils.HttpRequest
import com.example.android_steam_like.utils.ServerConfig
import org.json.JSONObject

class Wish (
    val id: String,
    val appid: String
) {
    companion object {
        private val wishlistEndpoint = ServerConfig.baseURL() + "/wishlist"

        fun addToWishlist (appid: String, callback: (res: String) -> Unit) {
            val body = JSONObject(mapOf("user" to "63aae4227a5a6755c421d4e7", "appid" to appid))
            HttpRequest(wishlistEndpoint,
                callback,
                "POST",
                body
            ).start()
        }

        fun removeFromWishlist(id: String, callback: (res: String) -> Unit) {
            HttpRequest(
                "$wishlistEndpoint/$id",
                callback,
                "DELETE"
            ).start()
        }
    }
}