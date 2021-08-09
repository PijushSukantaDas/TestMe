package com.silverblaze.myapplication.data.models

data class Response(
    val message: String,
    val user_list: List<User>
)