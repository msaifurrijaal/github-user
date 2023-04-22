package com.msaifurrijaal.submissiongithubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.msaifurrijaal.submissiongithubuser.databinding.ItemUserBinding
import com.msaifurrijaal.submissiongithubuser.model.ResponseItemFollow

class FollowAdapter: RecyclerView.Adapter<FollowAdapter.ViewHolder>() {

    private var followList = ArrayList<ResponseItemFollow>()

    fun setUser(newFollowList: List<ResponseItemFollow>) {
        followList.clear()
        followList.addAll(newFollowList)
        notifyDataSetChanged()
    }

    class ViewHolder(var binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = followList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var user = followList[position]

        Glide.with(holder.itemView)
            .load(user.avatarUrl)
            .into(holder.binding.ivUser)

        holder.binding.tvUsername.text = user.login
    }

}