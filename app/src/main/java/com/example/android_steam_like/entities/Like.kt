package layout

import com.example.android_steam_like.entities.User
import com.example.android_steam_like.utils.HttpRequest
import com.example.android_steam_like.utils.ServerConfig
import org.json.JSONObject

class Like (
    val id: String,
    val appId: String
) {
    companion object {
        private val likelistEndpoint = ServerConfig.baseURL() + "/likelist"

        fun getUserLikelist (callback: (res: String) -> Unit, simplified: Boolean = false) {
            val simplify = if (simplified) "?simplified=1" else ""
            HttpRequest("$likelistEndpoint/${User.getInstance()!!.id}$simplify", callback).start()
        }

        fun addToLikelist (appId: String?, callback: (res: String) -> Unit) {
            val body = JSONObject(mapOf("user" to User.getInstance()!!.id, "appid" to appId))
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