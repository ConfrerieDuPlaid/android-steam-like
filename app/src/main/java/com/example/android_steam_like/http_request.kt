package com.example.android_steam_like

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import kotlin.reflect.KFunction2



class http_request(val url: String, val callBack: (input: String) -> Unit, ) : Thread() {
    override fun run() {
        val client = OkHttpClient();
        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .build()
        request.header("Content")
        client.newCall(request).execute().use {
            try {
                val response = client.newCall(request).execute()
                var res = response.body?.string();
                if (res != null) {
                    callBack(res)
                };
            } catch (e: java.lang.Exception) {
                println("error : " + e.message)
            }
        }
    }
}