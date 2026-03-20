package com.example.myapplication.data.api.base

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(
    private val orgIdProvider: () -> String,
    private val authIdProvider: () -> String?
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val builder = originalRequest.newBuilder()
            .addHeader("organizer-id", orgIdProvider()) // ✅ always add org_id

        // Only add auth_id if available
        authIdProvider()?.let {
            builder.addHeader("access-token", it)
        }

        val newRequest = builder.build()
        return chain.proceed(newRequest)
    }
}
