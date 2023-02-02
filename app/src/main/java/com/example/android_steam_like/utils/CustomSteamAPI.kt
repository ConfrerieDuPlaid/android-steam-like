package com.example.android_steam_like.utils
import com.example.android_steam_like.entities.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import layout.AddWishLikeListBody
import layout.WishLikeData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class CustomSteamAPI {

    interface SteamCustomAPI {
        @GET("game/top100")
        fun getGamesTop100(): Deferred<MutableList<GameResponse>>

        @GET("game/{appId}")
        fun getGameById(@Path("appId") appId: String): Deferred<GameData>

        @GET("game?")
        fun getGamesByName(@Query("name") name: String): Deferred<MutableList<GameResponse>>

        @GET("comment/{appId}")
        fun getComment(@Path("appId") appId: String): Deferred<MutableList<Comment>>

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
            .create(SteamCustomAPI::class.java)

        suspend fun getGameTop100(x: Any? = null): MutableList<GameResponse> {
            return api.getGamesTop100().await()
        }

        suspend fun getGameById(appId: String): GameData {
            return api.getGameById(appId).await()
        }

        suspend fun searchGamesByName(name: String): MutableList<GameResponse> {
             return api.getGamesByName(name).await()
        }

        suspend fun getGameCommentById(appId: String): List<Comment>{
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