package com.silverblaze.myapplication.data.source

import com.silverblaze.myapplication.data.models.SingUp
import com.silverblaze.myapplication.data.models.Users
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface DataService {

    @GET("user/index")
    suspend fun userList() : Response<Users>

    @Multipart
    @POST("signup")
    suspend fun addOrSignup(
        @Part profile_image: MultipartBody.Part,
        @Part("name") name: HashMap<String, RequestBody>,
        @Part("email") email: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part("password_confirmation") password_confirmation: RequestBody

    ) : Response<SingUp>

    @Multipart
    @POST("signup")
    suspend fun register(
        @Part profile_image : MultipartBody.Part,
        @PartMap map :  HashMap<String , RequestBody>

    ) : Response<SingUp>
}