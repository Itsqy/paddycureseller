package com.darkcoder.paddycureseller.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.darkcoder.paddycure.data.network.ApiConfig
import com.darkcoder.paddycureseller.data.viewmodel.AddProductViewModel
import com.darkcoder.paddycureseller.data.viewmodel.EditProductViewModel
import com.darkcoder.paddycureseller.data.viewmodel.HomeViewModel
import com.darkcoder.paddycureseller.data.viewmodel.LoginViewModel

//import com.darkcoder.paddycure.data.viewmodel.LoginViewModel
//import com.darkcoder.paddycure.data.viewmodel.SplashViewModel

class ViewModelFactory(private val pref: UserPreferences, private val apiConfig: ApiConfig) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref, apiConfig) as T
            }

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(pref) as T
            }

            modelClass.isAssignableFrom(AddProductViewModel::class.java) -> {
                AddProductViewModel(pref) as T
            }

            modelClass.isAssignableFrom(EditProductViewModel::class.java) -> {
                EditProductViewModel(pref) as T
            }

//            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
//                SplashViewModel(pref) as T
//            }


            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

}