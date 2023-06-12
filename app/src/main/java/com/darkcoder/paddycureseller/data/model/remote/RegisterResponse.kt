package com.darkcoder.paddycureseller.data.model.remote

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

    @field:SerializedName("result")
    val result: Boolean,

    @field:SerializedName("keterangan")
    val keterangan: String,

    @field:SerializedName("data")
    val data: Data
)

data class Data(

    @field:SerializedName("img")
    val img: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("username")
    val username: String
)
