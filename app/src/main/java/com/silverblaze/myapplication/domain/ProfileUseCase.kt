package com.silverblaze.myapplication.domain

import com.silverblaze.myapplication.data.repository.ApiRepository
import javax.inject.Inject

class ProfileUseCase @Inject constructor(private val apiRepository: ApiRepository){
    fun getUserList() = apiRepository.getUserList()
}