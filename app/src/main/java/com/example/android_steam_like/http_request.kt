package com.example.android_steam_like

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class http_request(val url: String) : Thread() {
    override fun run() {
        val client = OkHttpClient();
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).execute().use {
            try {
                val response = client.newCall(request).execute()
                val res = JSONObject(response.body?.string());
            } catch (_: java.lang.Exception) { }
        }
    }
}