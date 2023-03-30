package com.kosmas.domain.repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.kosmas.data.BuildConfig
import okhttp3.ResponseBody

open class BaseRepository {

    private var errorCodePayload: Int? = null

    private fun getErrorAndDataObject(body: ResponseBody?): Pair<String, JsonObject?> {
        var error: String
        var data: JsonObject? = null
        if (body != null) {
            try {
                val json: JsonObject? = Gson().fromJson(body.charStream(), JsonObject::class.java)
                error = parseErrorsMessage(json) ?: "Error message empty"
                errorCodePayload = getResponseCode(json)

                if (json != null && json["data"] != null && json["data"].isJsonObject) {
                    data = json["data"].asJsonObject
                }
            } catch (e: Exception) {
                error =
                    "Oops! System on maintenance. please try again"
                if (BuildConfig.DEBUG) error = ("DEBUG: Body data is not Json Object")
                e.printStackTrace()
            }
        } else {
            error =
                "Oops! System on maintenance. please try again"
            if (BuildConfig.DEBUG) error = "DEBUG: Body data is null"
        }
        return Pair(error, data)
    }

    fun getErrorDataObject(body: ResponseBody?): JsonObject? {
        return getErrorAndDataObject(body).second
    }

    fun getErrorObject(body: ResponseBody?): String {
        return getErrorAndDataObject(body).first
    }

    private fun parseErrorsMessage(errorJson: JsonObject?): String? {
        return try {
            if (errorJson != null && errorJson["error"] != null) {
                if (errorJson["message"] != null && errorJson["message"].asString.isNotEmpty()) {
                    errorJson["message"].asString
                } else errorJson["error"].asString
            } else null
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        }
    }

    //Need update!
    private fun getResponseCode(json: JsonObject?): Int? {
        return try {
            if (json != null && json["code"] != null) {
                json["code"].asInt
            } else null
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        }
    }
}