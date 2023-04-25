package com.msaifurrijaal.submissiongithubuser.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.msaifurrijaal.submissiongithubuser.data.local.UserDAO
import com.msaifurrijaal.submissiongithubuser.data.local.UserFavoriteDatabase
import com.msaifurrijaal.submissiongithubuser.data.remote.GithubApiService
import com.msaifurrijaal.submissiongithubuser.data.remote.RetrofitService
import com.msaifurrijaal.submissiongithubuser.model.ResponseDetailUser
import com.msaifurrijaal.submissiongithubuser.model.ResponseItemFollow
import com.msaifurrijaal.submissiongithubuser.model.ResponseSearchUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(private val application: Application) {

    private val retrofit: GithubApiService
    private val dao: UserDAO

    init {
        retrofit = RetrofitService.getApiService()
        val dbUser: UserFavoriteDatabase = UserFavoriteDatabase.getInstance(application)
        dao = dbUser.userFavDao()
    }

    fun searchUser(q: String): LiveData<Resource<ResponseSearchUser>> {
        val responseUser = MutableLiveData<Resource<ResponseSearchUser>>()

        responseUser.postValue(Resource.Loading())
        retrofit.searchUsers(q).enqueue(object : Callback<ResponseSearchUser> {
            override fun onResponse(
                call: Call<ResponseSearchUser>,
                response: Response<ResponseSearchUser>
            ) {
                val responseItem = response.body()
                if (responseItem?.items.isNullOrEmpty()) {
                    responseUser.postValue(Resource.Error(null))
                } else {
                    responseUser.postValue(Resource.Success(responseItem))
                }
            }

            override fun onFailure(call: Call<ResponseSearchUser>, t: Throwable) {
                responseUser.postValue(Resource.Error(t.message))
            }
        })
        return responseUser
    }

    fun getFollowers(username: String): LiveData<Resource<List<ResponseItemFollow>>> {
        val followers = MutableLiveData<Resource<List<ResponseItemFollow>>>()

        followers.postValue(Resource.Loading())
        retrofit.getFollowers(username).enqueue(object : Callback<List<ResponseItemFollow>> {
            override fun onResponse(
                call: Call<List<ResponseItemFollow>>,
                response: Response<List<ResponseItemFollow>>
            ) {
                val list = response.body()
                if (list.isNullOrEmpty()) {
                    followers.postValue(Resource.Error(null))
                } else {
                    followers.postValue(Resource.Success(list))
                }
            }

            override fun onFailure(call: Call<List<ResponseItemFollow>>, t: Throwable) {
                followers.postValue(Resource.Error(t.message))
            }
        })
        return followers
    }

    fun getFollowing(username: String): LiveData<Resource<List<ResponseItemFollow>>> {
        val following = MutableLiveData<Resource<List<ResponseItemFollow>>>()

        following.postValue(Resource.Loading())
        retrofit.getFollowing(username).enqueue(object : Callback<List<ResponseItemFollow>> {
            override fun onResponse(
                call: Call<List<ResponseItemFollow>>,
                response: Response<List<ResponseItemFollow>>
            ) {
                val list = response.body()
                if (list.isNullOrEmpty()) {
                    following.postValue(Resource.Error(null))
                } else {
                    following.postValue(Resource.Success(list))
                }
            }

            override fun onFailure(call: Call<List<ResponseItemFollow>>, t: Throwable) {
                following.postValue(Resource.Error(t.message))
            }
        })
        return following
    }

    suspend fun getDetailUser(username: String): LiveData<Resource<ResponseDetailUser>> {
        val detailUser = MutableLiveData<Resource<ResponseDetailUser>>()

        retrofit.getDetailUser(username).enqueue(object : Callback<ResponseDetailUser> {
            override fun onResponse(
                call: Call<ResponseDetailUser>,
                response: Response<ResponseDetailUser>
            ) {
                if (response.isSuccessful) {
                    val detail = response.body()
                    detailUser.postValue(Resource.Success(detail))
                } else {
                    detailUser.postValue(Resource.Error(response.message()))
                }
            }

            override fun onFailure(call: Call<ResponseDetailUser>, t: Throwable) {
                detailUser.postValue(Resource.Error(t.message.toString()))
            }

        })
        return detailUser
    }

    suspend fun insertFavUser(user: ResponseDetailUser) = dao.upsertUser(user)

    suspend fun deleteFavUser(user: ResponseDetailUser) = dao.deleteUser(user)


}