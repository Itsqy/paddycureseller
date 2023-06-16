package com.darkcoder.paddycureseller.data.model.local

data class UserModel(
    val userName: String,
    val userId: String,
    val userToken: String,
    val isLogin: Boolean,
    val role: String
)