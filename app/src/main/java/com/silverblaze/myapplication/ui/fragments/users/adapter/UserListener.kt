package com.silverblaze.myapplication.ui.fragments.users.adapter

import com.silverblaze.myapplication.data.models.User

interface UserListener {
    fun deleteUser(user : User, position : Int)
}