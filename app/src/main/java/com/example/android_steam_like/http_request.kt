package com.example.android_steam_like

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class http_request(val url: String, val mainActivity: MainActivity, ) : Thread() {
    override fun run() {
        val client = OkHttpClient();
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use {
            try {
                val response = client.newCall(request).execute()
                val res = JSONObject(response.body?.string());
                println(res)
                mainActivity.addGame(Game("nathan", "théo", "12"))
            } catch (_: java.lang.Exception) { }
        }
    }
}