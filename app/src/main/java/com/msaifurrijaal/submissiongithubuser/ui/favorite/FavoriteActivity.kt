package com.msaifurrijaal.submissiongithubuser.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.msaifurrijaal.submissiongithubuser.Constants
import com.msaifurrijaal.submissiongithubuser.R
import com.msaifurrijaal.submissiongithubuser.data.Resource
import com.msaifurrijaal.submissiongithubuser.databinding.ActivityFavoriteBinding
import com.msaifurrijaal.submissiongithubuser.model.ResponseDetailUser
import com.msaifurrijaal.submissiongithubuser.ui.adapter.FavoriteUserAdapter
import com.msaifurrijaal.submissiongithubuser.ui.detail.DetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel by viewModels<FavoriteViewModel>()
    private lateinit var favUserAdapter: FavoriteUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFavUserAdapter()
        favUserObserve()
        onAction()

        supportActionBar?.setTitle("Favorite User")
    }

    private fun onAction() {
        favUserAdapter.onItemClick = {
            startActivity(
                Intent(this, DetailActivity::class.java)
                    .putExtra(Constants.USER_USERNAME, it.login)
            )
        }
    }

    private fun favUserObserve() {
        lifecycleScope.launch(Dispatchers.Main) {
            favoriteViewModel.getFavUserList().observe(this@FavoriteActivity, { response ->
                when(response) {
                    is Resource.Error -> errorAction(response)
                    is Resource.Loading -> loadingAction()
                    is Resource.Success -> response.data?.let {
                        successAction(it)
                    }
                }
            })
        }
    }

    private fun errorAction(message: Resource.Error<List<ResponseDetailUser>>) {
        if (message != null) {
            binding.apply {
                rvFavUser.visibility = View.INVISIBLE
                pgFav.visibility = View.INVISIBLE
                ivEmpty.visibility = View.VISIBLE
                if (message.message.toString() == "null") {
                    tvMessage.text = "Tidak ada user favorit!"
                } else {
                    tvMessage.text = message.message.toString()
                }
                tvMessage.visibility = View.VISIBLE
            }
        }
    }


    private fun loadingAction() {
        binding.apply {
            rvFavUser.visibility = View.INVISIBLE
            tvMessage.visibility = View.INVISIBLE
            ivEmpty.visibility = View.INVISIBLE
            pgFav.visibility = View.VISIBLE
        }
    }

    private fun successAction(listData: List<ResponseDetailUser>) {
        favUserAdapter.setUser(listData)
        binding.apply {
            pgFav.visibility = View.INVISIBLE
            ivEmpty.visibility = View.INVISIBLE
            tvMessage.visibility = View.INVISIBLE
            rvFavUser.visibility = View.VISIBLE
        }
    }

    private fun setFavUserAdapter() {
        favUserAdapter = FavoriteUserAdapter()
        binding.rvFavUser.apply {
            layoutManager = LinearLayoutManager(this@FavoriteActivity, LinearLayoutManager.VERTICAL, false)
            adapter = favUserAdapter
        }
    }

    override fun onRestart() {
        super.onRestart()
        favUserObserve()
    }
}