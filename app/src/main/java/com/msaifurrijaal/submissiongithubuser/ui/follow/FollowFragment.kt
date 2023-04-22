package com.msaifurrijaal.submissiongithubuser.ui.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.msaifurrijaal.submissiongithubuser.data.Resource
import com.msaifurrijaal.submissiongithubuser.databinding.FragmentFollowBinding
import com.msaifurrijaal.submissiongithubuser.model.ResponseItemFollow
import com.msaifurrijaal.submissiongithubuser.ui.adapter.FollowAdapter

class FollowFragment : Fragment() {

    private var binding: FragmentFollowBinding? = null
    private val followViewModel by viewModels<FollowViewModel>()
    private lateinit var followAdapter: FollowAdapter
    private var username: String? = null

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(inflater, container, false)

        username = arguments?.getString("username")

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)

        setRvFollow(index)
    }

    private fun setRvFollow(index: Int?) {
        followAdapter = FollowAdapter()
        binding!!.rvFollow.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = followAdapter
        }

        if (index == 1) {
            followViewModel.getFollowers(username!!).observe(viewLifecycleOwner, Observer { response ->
                when(response) {
                    is Resource.Error -> errorAction(response, "Follower")
                    is Resource.Loading -> loadingAction()
                    is Resource.Success -> response.data?.let {
                        successAction(it, "Follower")
                    }
                }
            })
        } else if (index == 2) {
            followViewModel.getFollowing(username!!).observe(viewLifecycleOwner, Observer { response ->
                when(response) {
                    is Resource.Error -> errorAction(response, "Following")
                    is Resource.Loading -> loadingAction()
                    is Resource.Success -> response.data?.let {
                        successAction(it, "Following")
                    }
                }
            })
        }
    }

    private fun successAction(listFollow: List<ResponseItemFollow>, typeFollow: String) {
        followAdapter.setUser(listFollow)
        binding!!.apply {
            if (listFollow.size == 0) {
                emptyAction(typeFollow)
                rvFollow.visibility = View.INVISIBLE
                tvMessage.visibility = View.VISIBLE
            } else {
                tvMessage.visibility = View.INVISIBLE
                rvFollow.visibility = View.VISIBLE
            }
            pgFollow.visibility = View.INVISIBLE
        }
    }

    private fun loadingAction() {
        binding!!.apply {
            tvMessage.visibility = View.INVISIBLE
            rvFollow.visibility = View.INVISIBLE
            pgFollow.visibility = View.VISIBLE
        }
    }

    private fun errorAction(response: Resource.Error<List<ResponseItemFollow>>, typeFollow: String) {
        binding!!.apply {
            if (response.data?.size == null) {
                emptyAction(typeFollow)
            } else {
                tvMessage.text = response.message
            }
            pgFollow.visibility = View.INVISIBLE
            rvFollow.visibility = View.INVISIBLE
            tvMessage.visibility = View.VISIBLE
        }
    }

    private fun emptyAction(typeFollow: String) {
        binding!!.apply {
            tvMessage.text = "User Tidak Memiliki $typeFollow"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}