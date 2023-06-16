package com.msaifurrijaal.submissiongithubuser.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.msaifurrijaal.submissiongithubuser.data.Repository
import com.msaifurrijaal.submissiongithubuser.data.Resource
import com.msaifurrijaal.submissiongithubuser.model.ResponseDetailUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel(application: Application): AndroidViewModel(application) {

    val repository = Repository(application)

    suspend fun getDetailUser(username: String) : LiveData<Resource<ResponseDetailUser>> = withContext(Dispatchers.IO) {
        var detailUser: LiveData<Resource<ResponseDetailUser>>? = null
        viewModelScope.launch {
            detailUser = repository.getDetailUser(username)
        }.join()
        return@withContext detailUser!!
    }

    fun inserFavUser(user: ResponseDetailUser) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFavUser(user)
        }
    }

    fun deleteFavUser(user: ResponseDetailUser) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavUser(user)
        }
    }

}