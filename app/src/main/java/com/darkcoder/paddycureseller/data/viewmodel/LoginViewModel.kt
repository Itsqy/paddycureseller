package com.darkcoder.paddycureseller.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.darkcoder.paddycure.data.model.local.UserModel
import com.darkcoder.paddycure.data.network.ApiConfig
import com.darkcoder.paddycureseller.data.model.remote.LoginResponse
import com.darkcoder.paddycureseller.utils.UserPreferences
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(val sharedPref: UserPreferences) : ViewModel() {

    private val _message = MutableLiveData<String>()
    val showMessage: LiveData<String> = _message


    private val _result = MutableLiveData<Boolean>()
    val showStatus: LiveData<Boolean> = _result

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(userame: String, passWord: String) {
        _isLoading.value = true
        val jsonObject = JSONObject()
        jsonObject.put("username", userame)
        jsonObject.put("password", passWord)
        val jsonObjectString = jsonObject.toString()

        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        ApiConfig.getApiService().login(requestBody).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                val result = response.body()?.result
                val ket = response.body()?.keterangan
                val user = response.body()?.user
                val body = response.body()


                if (response.isSuccessful) {
                    if (result != false) {
                        _isLoading.value = false
                        _result.value = response.body()?.result
                        _message.value = user?.username
                        val dataUser = body?.let {
                            UserModel(
                                user?.username.toString(),
                                user?.id.toString(),
                                it.token,
                                true
                            )
                        }

                        if (dataUser != null) {
                            saveUser(dataUser)
                        }

                        Log.d("loginData", "onResponse: ${result.toString()}")
                    } else {
                        _isLoading.value = false
                        _result.value = response.body()?.result
                        _message.value = ket.toString()
                        Log.d("loginDataNull", "onResponse: ${result.toString()}")
                    }

                } else {
                    _isLoading.value = false
                    Log.d("response not success", "onResponse:${response.body()?.result} ")
                }

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d("loginDataError", "onResponse: ${t.toString()}")
            }
        })
    }

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            sharedPref.saveUser(user)
        }
    }

    fun getUser(): LiveData<UserModel> {
        return sharedPref.getUser().asLiveData()
    }

    fun logOut() = viewModelScope.launch { sharedPref.logout() }


}