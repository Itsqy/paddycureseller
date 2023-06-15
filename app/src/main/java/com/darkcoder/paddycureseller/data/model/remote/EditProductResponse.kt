package com.darkcoder.paddycureseller.data.model.remote

import com.google.gson.annotations.SerializedName

data class EditProductResponse(

	@field:SerializedName("result")
	val result: Boolean,

	@field:SerializedName("keterangan")
	val keterangan: String
)
