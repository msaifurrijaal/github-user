package com.msaifurrijaal.submissiongithubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.msaifurrijaal.submissiongithubuser.databinding.ItemUserBinding
import com.msaifurrijaal.submissiongithubuser.model.ResponseItemSearch

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    lateinit var onItemClick: ((ResponseItemSearch) -> Unit)

    private var userList = ArrayList<ResponseItemSearch>()

    fun setUser(newUserList: List<ResponseItemSearch>) {
        userList.clear()
        userList.addAll(newUserList)
        notifyDataSetChanged()
    }

    class ViewHolder(var binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var user = userList[position]

        Glide.with(holder.itemView)
            .load(user.avatarUrl)
            .into(holder.binding.ivUser)

        holder.binding.tvUsername.text = user.login

        holder.itemView.setOnClickListener {
            onItemClick.invoke(user)
        }
    }

}