package com.example.android_steam_like.utils
import com.google.gson.annotations.SerializedName
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

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

    data class Sprites(
        @SerializedName("back_default")
        val backDefault: String,
    )

    interface PokemonAPI {
        @GET("game/top100")
        fun getGame(): Deferred<MutableList<GameResponse>>

    }

    object NetworkRequest {

        private val api = Retrofit.Builder()
            .baseUrl(ServerConfig.baseURL())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(PokemonAPI::class.java)

        suspend fun getPokemonsFromDitto(): MutableList<GameResponse> {
            return api.getGame().await()
        }

    }
}