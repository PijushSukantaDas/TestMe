package com.silverblaze.myapplication.domain

import com.silverblaze.myapplication.data.repository.ApiRepository
import java.io.File
import javax.inject.Inject

class AuthUseCase @Inject constructor(private val apiRepository: ApiRepository) {
    fun signUp(
        name: String,
        email: String,
        mobile: String,
        latitude: String,
        longitude: String,
        password: String,
        image : File
    )  = apiRepository.singUp(
        name,
        email,
        mobile,
        latitude,
        longitude,
        password,
        image
    )
}