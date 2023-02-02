package com.example.android_steam_like.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GenericAPI {
    companion object {
        suspend fun <ParameterType, ReturnType> call (apiFun: suspend (data: ParameterType) -> ReturnType, data: ParameterType, callback: (res: ReturnType) -> Unit) {
            val request = withContext(Dispatchers.IO) { apiFun(data) }
            callback(request)
        }
    }
}