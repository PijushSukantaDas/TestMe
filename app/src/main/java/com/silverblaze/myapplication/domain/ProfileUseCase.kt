package com.silverblaze.myapplication.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.silverblaze.myapplication.data.models.Delete
import com.silverblaze.myapplication.data.models.Profile
import com.silverblaze.myapplication.data.repository.ApiRepository
import retrofit2.Response
import javax.inject.Inject

class ProfileUseCase @Inject constructor(private val apiRepository: ApiRepository){

    fun getUserList() = apiRepository.getUserList()

    suspend fun profile(id : Int): Response<Profile> {
        return apiRepository.profile(id)
    }

    suspend fun delete(id : Int): Response<Delete> {
        return apiRepository.delete(id)
    }
}