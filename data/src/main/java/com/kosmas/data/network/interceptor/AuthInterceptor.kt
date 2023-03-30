package com.kosmas.data.network.interceptor

import com.google.gson.Gson
import com.kosmas.data.model.BaseResponse
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val gson: Gson
) : Interceptor {

    companion object {
        const val KEY_HEADER_CONTENT_TYPE = "Content-Type"
        const val KEY_HEADER_ACCEPT = "Accept"
        const val KEY_HEADER_USER_AGENT = "User-Agent"
        const val KEY_HEADER_AUTHORIZATION = "Authorization"
        const val KEY_APP_VERSION = "X-App-Version"
        const val KEY_HEADER_X_USER_AGENT = "x-user-agent"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .addHeader(KEY_HEADER_ACCEPT, "application/json")
            .method(originalRequest.method, originalRequest.body)
        requestBuilder.addHeader(KEY_HEADER_X_USER_AGENT, "Android")

        val newRequest = requestBuilder.build()
        return try {
            chain.proceed(newRequest)
        } catch (e: Exception) {
            val error = BaseResponse<Any?>(null, "${e.message}", "${e.message}", 500)
            val response = Response.Builder()
                .protocol(Protocol.HTTP_1_1)
                .message(("Network Exception Error : " + e.message) ?: "-")
                .request(newRequest)
                .code(500)
                .body(
                    ResponseBody.create(
                        "application/json".toMediaTypeOrNull(),
                        gson.toJson(error)
                    )
                )
            response.build()
        }
    }
}
