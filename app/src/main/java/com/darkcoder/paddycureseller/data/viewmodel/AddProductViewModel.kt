package com.darkcoder.paddycureseller.data.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.darkcoder.paddycureseller.data.model.local.UserModel
import com.darkcoder.paddycure.data.network.ApiConfig
import com.darkcoder.paddycureseller.data.model.remote.AddProductResponse
import com.darkcoder.paddycureseller.utils.UserPreferences
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddProductViewModel(private val userPreferences: UserPreferences) : ViewModel() {
    private val _addProductResponse = MutableLiveData<AddProductResponse>()
    val addProductResponse: LiveData<AddProductResponse> = _addProductResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUser(): LiveData<UserModel> {
        return userPreferences.getUser().asLiveData()
    }

    fun postProduct(
        imageMultipart: MultipartBody.Part,
        nama_produk: RequestBody,
        harga_produk: RequestBody,
        detail_produk: RequestBody,
        stok_pproduk: RequestBody
    ) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val uploadImageRequest = apiService.addProduct(
            imageMultipart,
            nama_produk,
            harga_produk,
            detail_produk,
            stok_pproduk
        )
        uploadImageRequest.enqueue(object : Callback<AddProductResponse> {
            override fun onResponse(
                call: Call<AddProductResponse>,
                response: Response<AddProductResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _addProductResponse.value = response.body()
                } else {
                    Log.e(ContentValues.TAG, "onFailure addStory: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<AddProductResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}