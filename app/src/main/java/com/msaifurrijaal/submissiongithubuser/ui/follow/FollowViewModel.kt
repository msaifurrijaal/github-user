package com.msaifurrijaal.submissiongithubuser.ui.follow

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.msaifurrijaal.submissiongithubuser.data.Repository

class FollowViewModel(application: Application): AndroidViewModel(application) {

    val repository = Repository(application)

    fun getFollowers(username: String) = repository.getFollowers(username)
    fun getFollowing(username: String) = repository.getFollowing(username)

}