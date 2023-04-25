package com.msaifurrijaal.submissiongithubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.*

import com.msaifurrijaal.submissiongithubuser.data.Repository
import com.msaifurrijaal.submissiongithubuser.data.Resource
import com.msaifurrijaal.submissiongithubuser.model.ResponseDetailUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FavoriteViewModel(application: Application): AndroidViewModel(application) {

    val repository = Repository(application)

    suspend fun getFavUserList() : LiveData<Resource<List<ResponseDetailUser>>> = withContext(Dispatchers.IO) {
        var favUserList: LiveData<Resource<List<ResponseDetailUser>>>? = null
        viewModelScope.launch {
            favUserList = repository.getFavUserList()
        }.join()
        return@withContext favUserList!!
    }

}