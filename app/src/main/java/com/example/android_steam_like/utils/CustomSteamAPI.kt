package com.example.android_steam_like.utils
import com.example.android_steam_like.entities.User
import com.google.gson.annotations.SerializedName
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

class CustomSteamAPI {
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


    data class WishLikeData(
        val _id: String,
        val appid: String,
        val user: String,
        val gameData: GameData?
    )


    data class Sprites(
        @SerializedName("back_default")
        val backDefault: String,
    )

    data class AddWishLikeListBody(
        val user: String,
        val appid: String
    )

    data class UserCredentials(
        val email: String,
        val password: String
    )

    data class UserSignupBody(
        val username: String,
        val email: String,
        val password: String
    )

    data class RecPassword(
        val token: String
    )

    data class ResPassword(
        val token: String,
        val password: String
    )

    data class Password(
        val password: String
    )

    interface SteamCustomAPI {
        @GET("game/top100")
        fun getGame(): Deferred<MutableList<GameResponse>>

        @GET("game/{appId}")
        fun getGameById(@Path("appId") appId: String): Deferred<GameData>

        @GET("comment/{appId}")
        fun getComment(@Path("appId") appId: String): Deferred<MutableList<comment>>

        @GET("wishlist/{userId}")
        fun getWishList(
            @Path("userId") userId: String,
            @Query("simplified") simplified: Int = 0
        ):  Deferred<List<WishLikeData>>

        @DELETE("wishlist/{id}")
        fun removeFromWishlist(@Path("id") id: String): Deferred<Int>

        @POST("wishlist")
        fun addToWishList(@Body body: AddWishLikeListBody): Deferred<WishLikeData>

        @GET("likelist/{userId}")
        fun getLikeList(
            @Path("userId") userId: String,
            @Query("simplified") simplified: Int = 0
        ):  Deferred<List<WishLikeData>>

        @DELETE("likelist/{id}")
        fun removeFromLikeList(@Path("id") id: String): Deferred<Int>

        @POST("likelist")
        fun addToLikeList(@Body body: AddWishLikeListBody): Deferred<WishLikeData>

        @POST("user/login")
        fun login (@Body body: UserCredentials): Deferred<User>

        @POST("user")
        fun signup (@Body body: UserSignupBody): Deferred<User>

        @PATCH("user/recPwd/{email}")
        fun requestToken (@Path("email") email: String): Deferred<RecPassword>

        @PATCH("user/{token}")
        fun resetPassword (@Path("token") password: String, @Body body: Password): Deferred<Int>
    }

    object NetworkRequest {

        private val api = Retrofit.Builder()
            .baseUrl(ServerConfig.baseURL())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(CustomSteamAPI.SteamCustomAPI::class.java)

        suspend fun getGameTop100(x: Any? = null): MutableList<CustomSteamAPI.GameResponse> {
            return api.getGame().await()
        }

        suspend fun getGameById(appId: String): CustomSteamAPI.GameData {
            return api.getGameById(appId).await()
        }

        suspend fun getGameCommentById(appId: String): List<comment>{
            return api.getComment(appId).await()
        }

        suspend fun getWihList(simplified: Int = 0): List<WishLikeData>{
            return api.getWishList(User.getInstance()!!.id, simplified).await()
        }

        suspend fun removeFromWishList(id: String): Int{
            return api.removeFromWishlist(id).await()
        }

        suspend fun addToWishList(id: String): WishLikeData{
            val body = AddWishLikeListBody(User.getInstance()!!.id, id)
            return api.addToWishList(body).await()
        }

        suspend fun getLikeList(simplified: Int = 0): List<WishLikeData>{
            return api.getLikeList(User.getInstance()!!.id, simplified).await()
        }

        suspend fun removeFromLikeList(id: String): Int{
            return api.removeFromLikeList(id).await()
        }

        suspend fun addToLikeList(id: String): WishLikeData{
            val body = AddWishLikeListBody(User.getInstance()!!.id, id)
            return api.addToLikeList(body).await()
        }

        suspend fun login (body: UserCredentials): User {
            return api.login(body).await()
        }

        suspend fun signup (body: UserSignupBody): User {
            return api.signup(body).await()
        }

        suspend fun requestToken (email: String): RecPassword {
            return api.requestToken(email).await()
        }

        suspend fun resetPassword (data: ResPassword): Int {
            val body = Password(data.password)
            return api.resetPassword(data.token, body).await()
        }
    }
}