package com.silverblaze.myapplication.data.repository

import com.silverblaze.myapplication.data.models.SingUp
import com.silverblaze.myapplication.data.models.Users
import com.silverblaze.myapplication.data.network.RetrofitInstance
import com.silverblaze.myapplication.data.network.Urls
import com.silverblaze.myapplication.data.source.DataService
import com.silverblaze.myapplication.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class ApiRepository @Inject constructor() {

    private val retrofit =
        RetrofitInstance().getRetrofitInstance(Urls.BASE_URL).create(DataService::class.java)
    var map : HashMap<String, RequestBody> = hashMapOf()


    fun getUserList() = flow<Resource<Users>> {
        val users = retrofit.userList().body()
        emit(Resource.success(users!!))
    }

    fun singUp(
        name: String,
        email: String,
        mobile: String,
        latitude: String,
        longitude: String,
        password: String,
        image: File
    ): Flow<Resource<SingUp>> {
        map["name"] = RequestBody.create(MultipartBody.FORM, name)
        map["gender"] = RequestBody.create(MultipartBody.FORM, "1")
        map["email"] = RequestBody.create(MultipartBody.FORM, email)
        map["phone"] = RequestBody.create(MultipartBody.FORM, mobile)
        map["latitude"] = RequestBody.create(MultipartBody.FORM, latitude)
        map["longitude"] = RequestBody.create(MultipartBody.FORM, longitude)
        map["password"] = RequestBody.create(MultipartBody.FORM, password)
        map["password_confirmation"] = RequestBody.create(MultipartBody.FORM, password)

        var reqFile = RequestBody.create("image/*".toMediaTypeOrNull(), image);


        return flow<Resource<SingUp>> {
            val signUp = retrofit.register(
                profile_image =   MultipartBody.Part.createFormData("image",image.getName(), reqFile),
                map
            ).body()
            emit(Resource.success(signUp!!))
        }
    }

}