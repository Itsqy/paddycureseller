package com.darkcoder.paddycureseller.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.darkcoder.paddycure.data.network.ApiConfig
import com.darkcoder.paddycureseller.data.model.remote.DataItem
import com.darkcoder.paddycureseller.data.model.remote.DeleteProductResponse
import com.darkcoder.paddycureseller.data.model.remote.ProductResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailsViewModel(): ViewModel() {
    private val _productDetails = MutableLiveData<DataItem>()
    val productDetails: LiveData<DataItem> = _productDetails

    private val _deleteProduct = MutableLiveData<DeleteProductResponse>()
    val deleteProduct: LiveData<DeleteProductResponse> = _deleteProduct

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getProductDetails(id: String) {
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

    fun deleteProduct(id: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().deleteProduct(id)
        client.enqueue(object : Callback<DeleteProductResponse> {
            override fun onResponse(
                call: Call<DeleteProductResponse>,
                response: Response<DeleteProductResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _deleteProduct.value = response.body()
                } else {
                    Log.e("null", "onViewCreated: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<DeleteProductResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("errorProduct", "onFailure: ${t.message}")
            }
        })
    }
}