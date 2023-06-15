package com.darkcoder.paddycureseller.data.model.remote

import com.google.gson.annotations.SerializedName

data class AddProductResponse(

	@field:SerializedName("result")
	val result: Boolean,

	@field:SerializedName("keterangan")
	val keterangan: String
)
