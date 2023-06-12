package com.darkcoder.paddycureseller.data.network

import com.darkcoder.paddycureseller.data.model.remote.LoginResponse
import com.darkcoder.paddycureseller.data.model.remote.RegisterResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {



    //    @Headers("Content-Type: application/json")
    @Multipart
    @POST("/users/register")
    fun register(
        @Part("nama") nama: String,
        @Part("username") username: String,
        @Part("password") password: String,
    ): Call<RegisterResponse>


    @Headers("Content-Type: application/json")
    @POST("/users/login")
    fun login(
        @Body requestBody: RequestBody
    ): Call<LoginResponse>



}
