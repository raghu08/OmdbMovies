package com.noonacademy.assignment.omdb.movies.api

import java.io.IOException
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

class CurlLoggingInterceptor @JvmOverloads constructor(private val logger: HttpLoggingInterceptor.Logger = HttpLoggingInterceptor.Logger.DEFAULT) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val url = original.url()
                .newBuilder()
                .addQueryParameter("apikey", "7294277f")
                .build()

        val requestBuilder = original.newBuilder().url(url)
        val request = requestBuilder.build()

        logger.log("--- cURL (" + request.url() + ")")
        logger.log("curl \"" + request.url() + "\"")
        logger.log("╰--- (copy and paste the above line to a terminal)")

        return chain.proceed(request)
    }

}