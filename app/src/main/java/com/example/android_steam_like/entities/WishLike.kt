package layout

import com.example.android_steam_like.entities.GameData


data class WishLikeData(
    val _id: String,
    val appid: String,
    val user: String,
    val gameData: GameData?
)

data class AddWishLikeListBody(
    val user: String,
    val appid: String
)