package com.darkcoder.paddycureseller.data.model.remote

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("result")
    val result: Boolean,

    @field:SerializedName("user")
    val user: User,

    @field:SerializedName("keterangan")
    val keterangan: String,

    @field:SerializedName("token")
    val token: String
)

data class User(

    @field:SerializedName("role")
    val role: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("username")
    val username: String
)