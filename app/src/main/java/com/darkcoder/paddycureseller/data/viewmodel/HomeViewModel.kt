package com.darkcoder.paddycureseller.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.darkcoder.paddycureseller.data.model.local.UserModel
import com.darkcoder.paddycure.data.network.ApiConfig
import com.darkcoder.paddycureseller.data.model.remote.DataItem
import com.darkcoder.paddycureseller.data.model.remote.ProductResponse
import com.darkcoder.paddycureseller.utils.UserPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    private val _listProduct = MutableLiveData<List<DataItem>>()
    val listProduct: LiveData<List<DataItem>> = _listProduct

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUser(): LiveData<UserModel> {
        return userPreferences.getUser().asLiveData()
    }

    fun getProduct(userId: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getProduct(userId)
        client.enqueue(object : Callback<ProductResponse> {
            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listProduct.value = response.body()?.data
                } else {
                    Log.e("null", "onViewCreated: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("errorProduct", "OnFailure: ${t.message}")
            }
        })
    }
}