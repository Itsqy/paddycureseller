package com.darkcoder.paddycureseller.data.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.darkcoder.paddycureseller.data.model.local.UserModel
import com.darkcoder.paddycure.data.network.ApiConfig
import com.darkcoder.paddycureseller.data.model.remote.DataItem
import com.darkcoder.paddycureseller.data.model.remote.EditProductResponse
import com.darkcoder.paddycureseller.data.model.remote.ProductResponse
import com.darkcoder.paddycureseller.utils.UserPreferences
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProductViewModel(private val userPreferences: UserPreferences) : ViewModel() {
    private val _editProductResponse = MutableLiveData<EditProductResponse>()
    val editProductResponse: LiveData<EditProductResponse> = _editProductResponse

    private val _productDetails = MutableLiveData<DataItem>()
    val productDetails: LiveData<DataItem> = _productDetails

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUser(): LiveData<UserModel> {
        return userPreferences.getUser().asLiveData()
    }

    fun editProduct(
        id: String,
        imageMultipart: MultipartBody.Part,
        nama_produk: RequestBody,
        harga_produk: RequestBody,
        detail_produk: RequestBody,
        stok_pproduk: RequestBody
    ) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val uploadImageRequest = apiService.updateProduct(
            id,
            imageMultipart,
            nama_produk,
            harga_produk,
            detail_produk,
            stok_pproduk
        )
        uploadImageRequest.enqueue(object : Callback<EditProductResponse> {
            override fun onResponse(
                call: Call<EditProductResponse>,
                response: Response<EditProductResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.e(ContentValues.TAG, "onSuccess edit: ${response.body()}")
                    _editProductResponse.value = response.body()
                } else {
                    Log.e(ContentValues.TAG, "onFailure edit: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<EditProductResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getProduct(id: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getProductDetails(id)
        client.enqueue(object : Callback<ProductResponse> {
            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _productDetails.value = response.body()?.data?.first()
                } else {
                    Log.e("null", "onViewCreated: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("errorProduct", "onFailure: ${t.message}")
            }
        })
    }
}