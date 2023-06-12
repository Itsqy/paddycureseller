package com.darkcoder.paddycureseller.data.model.remote

import com.google.gson.annotations.SerializedName

data class ProductResponse(

	@field:SerializedName("id_produk")
	val idProduk: String,

	@field:SerializedName("harga_produk")
	val hargaProduk: Int,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("detail_produk")
	val detailProduk: String,

	@field:SerializedName("stok_produk")
	val stokProduk: Int,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("img_produk")
	val imgProduk: String,

	@field:SerializedName("timestamp")
	val timestamp: String
)