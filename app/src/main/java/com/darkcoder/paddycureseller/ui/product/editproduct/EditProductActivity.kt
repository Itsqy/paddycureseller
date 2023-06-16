package com.darkcoder.paddycureseller.ui.product.editproduct

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.darkcoder.paddycure.data.network.ApiConfig
import com.darkcoder.paddycure.utils.createCustomTempFile
import com.darkcoder.paddycure.utils.reduceFileImage
import com.darkcoder.paddycure.utils.uriToFile
import com.darkcoder.paddycureseller.MainActivity
import com.darkcoder.paddycureseller.data.model.remote.DataItem
import com.darkcoder.paddycureseller.data.viewmodel.EditProductViewModel
import com.darkcoder.paddycureseller.databinding.ActivityEditProductBinding
import com.darkcoder.paddycureseller.ui.login.dataStore
import com.darkcoder.paddycureseller.utils.UserPreferences
import com.darkcoder.paddycureseller.utils.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EditProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProductBinding

    private var getFile: File? = null
    private lateinit var currentPhotoPath: String
    private lateinit var edNamaProduk: EditText
    private lateinit var edHargaProduk: EditText
    private lateinit var edDeskripsiProduk: EditText
    private lateinit var edStokProduk: EditText

    private val editProductViewModel: EditProductViewModel by viewModels {
        ViewModelFactory(UserPreferences.getInstance(this.dataStore), ApiConfig)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id").toString()

        supportActionBar?.hide()

        editProductViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        editProductViewModel.getProduct(id)
        editProductViewModel.productDetails.observe(this) {
            setProductDetails(it)
        }

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                EditProductActivity.REQUIRED_PERMISSIONS,
                EditProductActivity.REQUEST_CODE_PERMISSIONS
            )
        }

        edNamaProduk = binding.edProductNameEdit
        edDeskripsiProduk = binding.edProductDescEdit
        edHargaProduk = binding.edProductCostEdit
        edStokProduk = binding.edProductStockEdit

        binding.btnGalleryEdit.setOnClickListener { startGallery() }
        binding.btnCameraEdit.setOnClickListener { startCamera() }
        binding.btnSaveProduct.setOnClickListener { saveProduct(id) }

        binding.ivBackEdit.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setProductDetails(data: DataItem?) {
        Glide.with(this)
            .load(data?.imgProduk)
            .into(binding.ivPreviewEdit)

        binding.edProductNameEdit.setText(data?.namaProduk)
        binding.edProductStockEdit.setText(data?.stokProduk.toString())
        binding.edProductDescEdit.setText(data?.detailProduk)
        binding.edProductCostEdit.setText(data?.hargaProduk.toString())
    }

    private fun saveProduct(id: String) {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val title = edNamaProduk.text.toString().toRequestBody("text/plain".toMediaType())
            val cost = edHargaProduk.text.toString().toRequestBody("text/plain".toMediaType())
            val description = edDeskripsiProduk.text.toString().toRequestBody("text/plain".toMediaType())
            val stock = edStokProduk.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            editProductViewModel.editProduct(
                id,
                imageMultipart,
                title,
                cost,
                description,
                stock
            )
            editProductViewModel.editProductResponse.observe(this) {
                if (it.result) {
                    Toast.makeText(this@EditProductActivity, "Berhasil diubah!", Toast.LENGTH_SHORT).show()
                    val i = Intent(this@EditProductActivity, MainActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)
                    finish()
                }
            }
        } else {
            Toast.makeText(this@EditProductActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@EditProductActivity,
                "com.darkcoder.paddycureseller",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Silakan pilih gambar Anda")
        launcherIntentGallery.launch(chooser)
    }

    // intent camera
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            myFile.let { file ->
//                rotateFile(file)
                getFile = file
                binding.ivPreviewEdit.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    // launcher gallery
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@EditProductActivity)
                getFile = myFile
                binding.ivPreviewEdit.setImageURI(uri)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}