package com.darkcoder.paddycureseller.data.network

import com.darkcoder.paddycureseller.data.model.remote.AddProductResponse
import com.darkcoder.paddycureseller.data.model.remote.DeleteProductResponse
import com.darkcoder.paddycureseller.data.model.remote.EditProductResponse
import com.darkcoder.paddycureseller.data.model.remote.LoginResponse
import com.darkcoder.paddycureseller.data.model.remote.ProductResponse
import com.darkcoder.paddycureseller.data.model.remote.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

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


    @GET("produk/search/user_id/{user_id}")
    fun getProduct(@Path("user_id") userId: String) : Call<ProductResponse>

    @GET("produk/search/id/{id}")
    fun getProductDetails(@Path("id") id: String) : Call<ProductResponse>

    @Multipart
    @POST("produk/insert-produk")
    fun addProduct(
        @Part file: MultipartBody.Part,
        @Part("nama_produk") nama_produk: RequestBody,
        @Part("harga_produk") harga_produk: RequestBody,
        @Part("detail_produk") detail_produk: RequestBody,
        @Part("stok_produk") stok_produk: RequestBody,
    ) : Call<AddProductResponse>

    @Multipart
    @PUT("produk/update/{id}")
    fun updateProduct(
        @Path("id") id: String,
        @Part file: MultipartBody.Part,
        @Part("nama_produk") nama_produk: RequestBody,
        @Part("harga_produk") harga_produk: RequestBody,
        @Part("detail_produk") detail_produk: RequestBody,
        @Part("stok_produk") stok_produk: RequestBody,
    ) : Call<EditProductResponse>

    @DELETE("produk/delete/{id}")
    fun deleteProduct(@Path("id") id: String) : Call<DeleteProductResponse>
}
