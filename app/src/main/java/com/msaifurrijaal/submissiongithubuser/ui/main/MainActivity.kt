package com.msaifurrijaal.submissiongithubuser.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.msaifurrijaal.submissiongithubuser.Constants.TYPE_INTENT
import com.msaifurrijaal.submissiongithubuser.Constants.USER_API
import com.msaifurrijaal.submissiongithubuser.Constants.USER_USERNAME
import com.msaifurrijaal.submissiongithubuser.R
import com.msaifurrijaal.submissiongithubuser.data.Resource
import com.msaifurrijaal.submissiongithubuser.data.datastore.SettingPreferences
import com.msaifurrijaal.submissiongithubuser.databinding.ActivityMainBinding
import com.msaifurrijaal.submissiongithubuser.model.ResponseItemSearch
import com.msaifurrijaal.submissiongithubuser.model.ResponseSearchUser
import com.msaifurrijaal.submissiongithubuser.ui.adapter.UserAdapter
import com.msaifurrijaal.submissiongithubuser.ui.detail.DetailActivity
import com.msaifurrijaal.submissiongithubuser.ui.favorite.FavoriteActivity
import com.msaifurrijaal.submissiongithubuser.ui.setting.SettingActivity
import com.msaifurrijaal.submissiongithubuser.ui.setting.SettingViewModel
import com.msaifurrijaal.submissiongithubuser.ui.setting.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var settingViewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        settingViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(SettingViewModel::class.java)

        settingTheme()
        setUserRv()
        onAction()

    }

    private fun settingTheme() {
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun onAction() {
        binding.etSearchMain.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    // logika yang ingin dilakukan ketika pengguna menekan tombol search
                    clearFocus()
                    binding.ivSearch.visibility = View.INVISIBLE
                    binding.tvMessage.visibility = View.INVISIBLE
                    mainViewModel.searchUser(query!!).observe(this@MainActivity, { response ->
                        when(response) {
                            is Resource.Error -> errorAction(response)
                            is Resource.Loading -> loadingAction()
                            is Resource.Success -> response.data?.let {
                                successAction(it)
                            }
                        }
                    })
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    // logika yang ingin dilakukan ketika pengguna mengubah teks pada EditText
                    return true
                }
            })
        }

        userAdapter.onItemClick = {
            startActivity(
                Intent(this, DetailActivity::class.java)
                    .putExtra(USER_USERNAME, it.login)
            )
        }

    }

    private fun errorAction(errorMessage: Resource.Error<ResponseSearchUser>) {
        if (errorMessage != null) {
            binding.apply {
                pgMain.visibility = View.INVISIBLE
                rvUser.visibility = View.INVISIBLE
                ivSearch.visibility = View.INVISIBLE
                ivEmpty.visibility = View.VISIBLE
                tvMessage.text = errorMessage.message.toString()
                tvMessage.visibility = View.VISIBLE
            }
        }
    }

    private fun successAction(listUser: ResponseSearchUser) {
        userAdapter.setUser(listUser.items as List<ResponseItemSearch>)
        binding.apply {
            pgMain.visibility = View.INVISIBLE
            ivEmpty.visibility = View.INVISIBLE
            ivSearch.visibility = View.INVISIBLE
            tvMessage.visibility = View.INVISIBLE
            rvUser.visibility = View.VISIBLE
        }
    }


    private fun loadingAction() {
        binding.apply {
            rvUser.visibility = View.INVISIBLE
            ivEmpty.visibility = View.INVISIBLE
            ivSearch.visibility = View.INVISIBLE
            tvMessage.visibility = View.INVISIBLE
            pgMain.visibility = View.VISIBLE

        }
    }



    private fun setUserRv() {
        userAdapter = UserAdapter()
        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = userAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        for (i in 0 until menu!!.size()) {
            val drawable = menu.getItem(i).icon
            drawable!!.mutate()
            drawable.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu1 -> {
                val i = Intent(this, FavoriteActivity::class.java)
                startActivity(i)
                return true
            }
            R.id.menu2 -> {
                val i = Intent(this, SettingActivity::class.java)
                startActivity(i)
                return true
            }
            else -> return true
        }
    }

}