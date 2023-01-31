package com.example.android_steam_like.utils
import com.example.android_steam_like.entities.User
import com.google.gson.annotations.SerializedName
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class steamApi {
    data class GameData(
        val name: String,
        val publishers: List<String>,
        val priceInCents: Double,
        val steamAppid: Int,
        val headerImage: String,
        val description: String?,
        val backgroundImage: String?,
        val screenshots: MutableList<String>,
    )

    data class GameResponse(
        val rank: Int,
        val appId: Int,
        val gameData: GameData
    )

    data class comment(
        val author: String,
        val score: String,
        val content: String,
    )


    data class WishList(
        val _id: String,
        val appId: String,
        val user: String,
        val gameData: GameData?
    )


    data class Sprites(
        @SerializedName("back_default")
        val backDefault: String,
    )

    interface PokemonAPI {
        @GET("game/top100")
        fun getGame(): Deferred<MutableList<GameResponse>>


        @GET("game/{appId}")
        fun getGameById(@Path("appId") appId: String): Deferred<GameData>

        @GET("comment/{appId}")
        fun getComment(@Path("appId") appId: String): Deferred<MutableList<comment>>

        @GET("/wishlist/{userId}")
        fun getWishList(
            @Path("userId") userId: String,
            @Query("simplified") simplified: Int = 0
        ):  Deferred<List<WishList>>

    }

    object NetworkRequest {

        private val api = Retrofit.Builder()
            .baseUrl(ServerConfig.baseURL())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(PokemonAPI::class.java)

        suspend fun getGameTop1000(): MutableList<GameResponse> {
            return api.getGame().await()
        }

        suspend fun getGameById(appId: String): GameData{
            return api.getGameById(appId).await()
        }

        suspend fun getGameCommentById(appId: String): List<comment>{
            return api.getComment(appId).await()
        }

        suspend fun getWihList(simplified: Int = 0): List<WishList>{
            return api.getWishList(User.getInstance()!!.id, simplified).await()
        }
    }
}