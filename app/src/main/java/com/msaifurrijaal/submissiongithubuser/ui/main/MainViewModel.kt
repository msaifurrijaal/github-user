package com.msaifurrijaal.submissiongithubuser.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.msaifurrijaal.submissiongithubuser.data.Repository

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository = Repository(application)

    fun searchUser(q: String) = repository.searchUser(q)


}