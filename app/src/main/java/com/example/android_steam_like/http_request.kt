package com.example.android_steam_like

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import kotlin.reflect.KFunction2



class http_request(val url: String, val callBack: (input: Game) -> Unit, ) : Thread() {
    override fun run() {
        val client = OkHttpClient();
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).execute().use {
            try {
                val response = client.newCall(request).execute()
                var res = JSONObject(response.body?.string());
                val appId = url.split("=").last();
                res = res.getJSONObject(appId).getJSONObject("data");
                val gameName = res.getString("name")
                val editors = res.getJSONArray("publishers")
                val price = res.getString("price_in_cents_with_discount")
                println("coucou")
                callBack(Game(gameName, editors[0].toString(), ""))
            } catch (_: java.lang.Exception) { }
        }
    }
}