package com.msaifurrijaal.submissiongithubuser.data.remote

import com.msaifurrijaal.submissiongithubuser.model.ResponseDetailUser
import com.msaifurrijaal.submissiongithubuser.model.ResponseItemFollow
import com.msaifurrijaal.submissiongithubuser.model.ResponseSearchUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {

    @GET("search/users")
    fun searchUsers (
        @Query("q")
        query: String
    ): Call<ResponseSearchUser>

    @GET("users/{username}")
    fun getDetailUser (
        @Path("username")
        username: String
    ): Call<ResponseDetailUser>

    @GET("users/{username}/followers")
    fun getFollowers (
        @Path("username")
        username: String
    ): Call<List<ResponseItemFollow>>

    @GET("users/{username}/following")
    fun getFollowing (
        @Path("username")
        username: String
    ): Call<List<ResponseItemFollow>>

}