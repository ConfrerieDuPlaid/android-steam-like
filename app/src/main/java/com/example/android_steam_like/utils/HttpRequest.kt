package com.example.android_steam_like.utils

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


class HttpRequest(
    private val url: String,
    val callbackSuccess: (input: String) -> Unit,
    private val method: String = "GET",
    private val bodyJson: JSONObject? = null,
    val callbackError: (e: Int) -> Unit = ::println,
) : Thread() {
    private val allowedMethods: List<String> = listOf("GET", "POST", "PATCH", "DELETE")
    private val mediaType = "application/json; charset=utf-8".toMediaType()
    private val client = OkHttpClient()

    override fun run() {
        if (!this.methodIsAllowed(method)) return

        val request = this.buildRequest(url, method, bodyJson)
        request.header("Content")
        client.newCall(request).execute().use {
            try {
                val response = it
                val res = response.body?.string()
                if (response.code < 400) {
                    if (res != null) {
                        callbackSuccess(res)
                    }
                } else {
                    callbackError(response.code)
                }
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    private fun methodIsAllowed(method: String): Boolean {
        return allowedMethods.contains(method)
    }

    private fun buildRequest (url: String, method: String, bodyJson: JSONObject?): Request {
        var requestBuilder = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")

        requestBuilder = if (method == "PATCH" || method == "POST") {
            val body: RequestBody = bodyJson.toString().toRequestBody(mediaType)
            requestBuilder.method(method, body)
        } else {
            requestBuilder.method(method, null)
        }

        return requestBuilder.build()
    }
}