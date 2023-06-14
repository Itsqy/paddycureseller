package com.darkcoder.paddycureseller.ui.product.productdetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.darkcoder.paddycureseller.MainActivity
import androidx.appcompat.app.AppCompatDelegate
import com.darkcoder.paddycureseller.R
import com.darkcoder.paddycureseller.data.model.remote.DataItem
import com.darkcoder.paddycureseller.data.viewmodel.ProductDetailsViewModel
import com.darkcoder.paddycureseller.databinding.ActivityProductDetailsBinding
import com.darkcoder.paddycureseller.ui.product.addproduct.AddProductActivity
import com.darkcoder.paddycureseller.ui.product.editproduct.EditProductActivity

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding
    private val productDetailsViewModel: ProductDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val id = intent.getStringExtra("id").toString()

        supportActionBar?.hide()

        productDetailsViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        productDetailsViewModel.getProductDetails(id)
        productDetailsViewModel.productDetails.observe(this) {
            setProductDetails(it)
        }

        binding.btnToEdit.setOnClickListener {
            startActivity(Intent(this, EditProductActivity::class.java).putExtra("id", id))
        }

        binding.btnDelete.setOnClickListener {
            productDetailsViewModel.deleteProduct(id)
            Toast.makeText(this, "Berhasil dihapus!", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(this@ProductDetailsActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        binding.ivBackDetails.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setProductDetails(data: DataItem?) {
        Glide.with(this)
            .load(data?.imgProduk)
            .into(binding.ivProductImg)

        binding.tvProductTitle.text = data?.namaProduk
        binding.tvCountSold.text = "${data?.stokProduk} Tersedia"
        binding.tvProductDesc.text = data?.detailProduk
        binding.tvCost.text = "Rp. ${data?.hargaProduk}"
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}