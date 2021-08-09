package com.silverblaze.myapplication.data.source

import com.silverblaze.myapplication.data.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface DataService {

    @GET("user/index")
    suspend fun userList() : Response<Users>

    @Multipart
    @POST("signup")
    suspend fun signup(
        @Part profile_image : MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part("password_confirmation") password_confirmation: RequestBody
    ) : Response<SingUp>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Response<Login>


    @GET("user/{id}")
    suspend fun profile(
        @Path(value = "id", encoded = false)
        id : Int,
    ) : Response<Profile>

    @Multipart
    @POST("signup")
    suspend fun addNewUser(
        @Part profile_image : MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part("password_confirmation") password_confirmation: RequestBody
    ) : Response<NewUser>

    @GET("user/delete/{id}")
    suspend fun delete(
        @Path(value = "id", encoded = false)
        id : Int,
    ) : Response<Delete>
}