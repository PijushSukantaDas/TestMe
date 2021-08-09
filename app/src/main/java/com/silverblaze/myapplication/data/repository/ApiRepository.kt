package com.silverblaze.myapplication.data.repository

import android.content.ContentResolver
import android.net.Uri
import com.silverblaze.myapplication.data.models.*
import com.silverblaze.myapplication.data.network.RetrofitInstance
import com.silverblaze.myapplication.data.network.Urls
import com.silverblaze.myapplication.data.source.DataService
import com.silverblaze.myapplication.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType
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
        map["name"] = RequestBody.create("text/plain".toMediaTypeOrNull(),name)
        map["gender"] = RequestBody.create("text/plain".toMediaTypeOrNull(),"1")
        map["email"] = RequestBody.create("text/plain".toMediaTypeOrNull(),email)
        map["phone"] = RequestBody.create("text/plain".toMediaTypeOrNull(),mobile)
        map["latitude"] = RequestBody.create("text/plain".toMediaTypeOrNull(),latitude)
        map["longitude"] = RequestBody.create("text/plain".toMediaTypeOrNull(),longitude)
        map["password"] = RequestBody.create("text/plain".toMediaTypeOrNull(),password)
        map["password_confirmation"] = RequestBody.create("text/plain".toMediaTypeOrNull(),password)

        val requestFile = MultipartBody.Part.createFormData("profile_image",image.name,RequestBody.create("image/*".toMediaTypeOrNull(),image))
//        val body = Part.createFormData("profile_picture", image.name, requestFile)


        return flow<Resource<SingUp>> {
            val signUp = retrofit.signup(
                profile_image =   requestFile,
                name = RequestBody.create("text/plain".toMediaTypeOrNull(),name),
                gender = RequestBody.create("text/plain".toMediaTypeOrNull(),"1"),
                email= RequestBody.create("text/plain".toMediaTypeOrNull(),email),
                phone = RequestBody.create("text/plain".toMediaTypeOrNull(),mobile),
                latitude = RequestBody.create("text/plain".toMediaTypeOrNull(),latitude),
                longitude = RequestBody.create("text/plain".toMediaTypeOrNull(),longitude),
                password = RequestBody.create("text/plain".toMediaTypeOrNull(),password),
                password_confirmation = RequestBody.create("text/plain".toMediaTypeOrNull(),password)
            ).body()

            emit(Resource.success(signUp!!))
        }
    }

    fun login(email: String, password: String) = flow<Resource<Login>> {
        val response = retrofit.login(
            email,
            password
        ).body()

        emit(Resource.success(response!!))
    }

    fun addNewUser(
        name: String,
        email: String,
        mobile: String,
        latitude: String,
        longitude: String,
        password: String,
        image: File
    ): Flow<Resource<NewUser>> {
        map["name"] = RequestBody.create("text/plain".toMediaTypeOrNull(),name)
        map["gender"] = RequestBody.create("text/plain".toMediaTypeOrNull(),"1")
        map["email"] = RequestBody.create("text/plain".toMediaTypeOrNull(),email)
        map["phone"] = RequestBody.create("text/plain".toMediaTypeOrNull(),mobile)
        map["latitude"] = RequestBody.create("text/plain".toMediaTypeOrNull(),latitude)
        map["longitude"] = RequestBody.create("text/plain".toMediaTypeOrNull(),longitude)
        map["password"] = RequestBody.create("text/plain".toMediaTypeOrNull(),password)
        map["password_confirmation"] = RequestBody.create("text/plain".toMediaTypeOrNull(),password)

        val requestFile = MultipartBody.Part.createFormData("profile_image",image.name,RequestBody.create("image/*".toMediaTypeOrNull(),image))
//        val body = Part.createFormData("profile_picture", image.name, requestFile)


        return flow<Resource<NewUser>> {
            val signUp = retrofit.addNewUser(
                profile_image =   requestFile,
                name = RequestBody.create("text/plain".toMediaTypeOrNull(),name),
                gender = RequestBody.create("text/plain".toMediaTypeOrNull(),"1"),
                email= RequestBody.create("text/plain".toMediaTypeOrNull(),email),
                phone = RequestBody.create("text/plain".toMediaTypeOrNull(),mobile),
                latitude = RequestBody.create("text/plain".toMediaTypeOrNull(),latitude),
                longitude = RequestBody.create("text/plain".toMediaTypeOrNull(),longitude),
                password = RequestBody.create("text/plain".toMediaTypeOrNull(),password),
                password_confirmation = RequestBody.create("text/plain".toMediaTypeOrNull(),password)
            ).body()

            emit(Resource.success(signUp!!))
        }
    }

    suspend fun profile(id : Int) = retrofit.profile(id)

    suspend fun delete(id : Int) = retrofit.delete(id)

}