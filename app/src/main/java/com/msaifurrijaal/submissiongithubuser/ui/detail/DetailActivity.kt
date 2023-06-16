package com.msaifurrijaal.submissiongithubuser.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.msaifurrijaal.submissiongithubuser.Constants.USER_USERNAME
import com.msaifurrijaal.submissiongithubuser.R
import com.msaifurrijaal.submissiongithubuser.data.Resource
import com.msaifurrijaal.submissiongithubuser.databinding.ActivityDetailBinding
import com.msaifurrijaal.submissiongithubuser.model.ResponseDetailUser
import com.msaifurrijaal.submissiongithubuser.ui.adapter.SectionsPagerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var sectionPagerAdapter: SectionsPagerAdapter
    private val detailViewModel by viewModels<DetailViewModel>()
    private var username: String = ""

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        receiveIntent()
        setInformationUser()
        setViewPager()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(username)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setViewPager() {
        sectionPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        val tabs: TabLayout = findViewById(R.id.tabs)
        sectionPagerAdapter.setMyString(username)
        viewPager.adapter = sectionPagerAdapter

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setInformationUser() {
        loadingAction()
        username?.let {
            lifecycleScope.launch(Dispatchers.Main) {
                detailViewModel.getDetailUser(username).observe(this@DetailActivity, { response ->
                    when(response) {
                        is Resource.Error -> errorAction(response)
                        is Resource.Loading -> loadingAction()
                        is Resource.Success -> response.data?.let {
                            successAction(it)
                        }

                        else -> {}
                    }
                })
            }
        }
    }

    private fun successAction(userProfil: ResponseDetailUser) {
        binding.apply {
            Glide.with(this@DetailActivity)
                .load(userProfil.avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.ivUser)

            tvName.text = userProfil.name
            tvCompanyName.text = userProfil.company
            tvCountFollower.text = userProfil.followers.toString()
            tvCountFollowing.text = userProfil.following.toString()

            if (userProfil.isFavorite == true)
                ivFavorite.setImageDrawable(resources.getDrawable(R.drawable.baseline_favorite_24))
            else
                ivFavorite.setImageDrawable(resources.getDrawable(R.drawable.baseline_favorite_border_24))

            ivFavorite.setOnClickListener {
                if (userProfil.isFavorite == true) {
                    userProfil.isFavorite = false
                    detailViewModel.deleteFavUser(userProfil)
                    ivFavorite.setImageDrawable(resources.getDrawable(R.drawable.baseline_favorite_border_24))
                } else {
                    userProfil.isFavorite = true
                    userProfil?.let {
                        detailViewModel.inserFavUser(it)
                    }
                    ivFavorite.setImageDrawable(resources.getDrawable(R.drawable.baseline_favorite_24))
                }
            }
            responseAction()
        }
    }

    private fun errorAction(errorMessage: Resource.Error<ResponseDetailUser>) {
        binding.apply {
            ivUser.visibility = View.INVISIBLE
            tvName.visibility = View.INVISIBLE
            tvCompanyName.visibility = View.INVISIBLE
            tvFollower.visibility = View.INVISIBLE
            tvFollowing.visibility = View.INVISIBLE
            tvCountFollower.visibility = View.INVISIBLE
            tvCountFollowing.visibility = View.INVISIBLE
            pgDetail.visibility = View.INVISIBLE
            ivFavorite.visibility = View.INVISIBLE
            ivErrorMessage.visibility = View.VISIBLE
            tvMessage.visibility = View.VISIBLE
        }
    }

    private fun responseAction() {
        binding.apply {
            pgDetail.visibility = View.INVISIBLE
            ivErrorMessage.visibility = View.INVISIBLE
            tvMessage.visibility = View.INVISIBLE
            ivUser.visibility = View.VISIBLE
            tvName.visibility = View.VISIBLE
            tvCompanyName.visibility = View.VISIBLE
            tvFollower.visibility = View.VISIBLE
            tvFollowing.visibility = View.VISIBLE
            tvCountFollower.visibility = View.VISIBLE
            tvCountFollowing.visibility = View.VISIBLE
            ivFavorite.visibility = View.VISIBLE
        }
    }

    private fun loadingAction() {
        binding.apply {
            ivUser.visibility = View.INVISIBLE
            tvName.visibility = View.INVISIBLE
            tvCompanyName.visibility = View.INVISIBLE
            tvFollower.visibility = View.INVISIBLE
            tvFollowing.visibility = View.INVISIBLE
            tvCountFollower.visibility = View.INVISIBLE
            tvCountFollowing.visibility = View.INVISIBLE
            ivErrorMessage.visibility = View.INVISIBLE
            tvMessage.visibility = View.INVISIBLE
            ivFavorite.visibility = View.INVISIBLE
            pgDetail.visibility = View.VISIBLE
        }
    }

    private fun receiveIntent() {
        username = intent.getStringExtra(USER_USERNAME)!!
    }
}