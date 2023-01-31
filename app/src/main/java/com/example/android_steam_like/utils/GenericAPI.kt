package com.example.android_steam_like.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GenericAPI {
    companion object {
        suspend fun <K,T> call (apiFun: suspend (data: K) -> T, data: K, callback: (res: T) -> Unit) {
            try {
                val request = withContext(Dispatchers.IO) { apiFun(data) }
                callback(request)
            } catch (e: Exception) {
                println("Une erreur est survenue ${e.message}")
            }
        }
    }
}