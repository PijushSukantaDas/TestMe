package com.silverblaze.myapplication.data.models

data class User(
    val created_at: String,
    val email: String,
    val email_verified_at: String,
    val gender: String,
    val id: Int,
    val latitude: String,
    val longitude: String,
    val name: String,
    val password: String,
    val phone: String,
    val profile_image: String,
    val updated_at: String
)