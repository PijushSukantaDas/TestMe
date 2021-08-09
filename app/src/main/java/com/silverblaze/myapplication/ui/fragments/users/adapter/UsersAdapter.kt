package com.silverblaze.myapplication.ui.fragments.users.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.silverblaze.myapplication.R
import com.silverblaze.myapplication.data.models.User
import com.silverblaze.myapplication.databinding.UserCardLayoutBinding
import com.squareup.picasso.Picasso

class UsersAdapter(private val userList : List<User>) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    class UserViewHolder(private val binding: UserCardLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(user: User) {
            binding.name.text = user.name
            binding.email.text = user.email
            if (user.profile_image.isNotEmpty()){
                Picasso.get()
                    .load(user.profile_image)
                    .fit()
                    .centerCrop()
                    .into(binding.image)
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapter.UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<UserCardLayoutBinding>(layoutInflater, R.layout.user_card_layout,parent,false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersAdapter.UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount() = userList.size
}