package com.example.android_steam_like.entities

class User (
    val id: String,
    val username: String,
    val email: String
        ) {

    companion object {
        private var INSTANCE: User? = null

        fun setInstance (u: User) {
            INSTANCE = u
        }

        fun getInstance (): User? {
            return INSTANCE
        }
    }
}

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