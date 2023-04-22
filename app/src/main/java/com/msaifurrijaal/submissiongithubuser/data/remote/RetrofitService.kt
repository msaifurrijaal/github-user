package com.msaifurrijaal.submissiongithubuser.data.remote

import com.msaifurrijaal.submissiongithubuser.Constants.BASE_URL
import com.msaifurrijaal.submissiongithubuser.Constants.GITHUB_TOKEN
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {

    val authInterceptor = Interceptor { chain ->
        val req = chain.request()
        val requestHeaders = req.newBuilder()
            .addHeader("Authorization", GITHUB_TOKEN)
            .build()
        chain.proceed(requestHeaders)
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    fun getApiService(): GithubApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(GithubApiService::class.java)
    }

}