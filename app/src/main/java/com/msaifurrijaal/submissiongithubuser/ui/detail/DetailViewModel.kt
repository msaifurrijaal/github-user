package com.msaifurrijaal.submissiongithubuser.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.msaifurrijaal.submissiongithubuser.data.Repository
import com.msaifurrijaal.submissiongithubuser.data.Resource
import com.msaifurrijaal.submissiongithubuser.model.ResponseDetailUser
import kotlinx.coroutines.launch

class DetailViewModel(application: Application): AndroidViewModel(application) {

    val repository = Repository(application)

    fun getDetailUser(username: String) : LiveData<Resource<ResponseDetailUser>> {
        var detailUser: LiveData<Resource<ResponseDetailUser>>? = null
        viewModelScope.launch {
            detailUser = repository.getDetailUser(username)
        }
        return detailUser!!
    }

    fun inserFavUser(user: ResponseDetailUser) {
        viewModelScope.launch {
            repository.insertFavUser(user)
        }
    }

    fun deleteFavUser(user: ResponseDetailUser) {
        viewModelScope.launch {
            repository.deleteFavUser(user)
        }
    }

}