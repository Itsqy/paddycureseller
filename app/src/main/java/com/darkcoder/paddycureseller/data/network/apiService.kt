package com.darkcoder.paddycureseller.data.network

import com.darkcoder.paddycureseller.data.model.remote.ProductResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("product")
    fun getProduct() : Call<ProductResponse>
}