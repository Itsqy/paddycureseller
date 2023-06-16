package com.darkcoder.paddycureseller.ui.product.addproduct

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.darkcoder.paddycure.data.network.ApiConfig
import com.darkcoder.paddycure.utils.createCustomTempFile
import com.darkcoder.paddycure.utils.reduceFileImage
import com.darkcoder.paddycure.utils.uriToFile
import com.darkcoder.paddycureseller.MainActivity
import com.darkcoder.paddycureseller.data.viewmodel.AddProductViewModel
import com.darkcoder.paddycureseller.databinding.ActivityAddProductBinding
import com.darkcoder.paddycureseller.ui.login.dataStore
import com.darkcoder.paddycureseller.utils.UserPreferences
import com.darkcoder.paddycureseller.utils.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductBinding
    private var getFile: File? = null
    private lateinit var currentPhotoPath: String
    private lateinit var edNamaProduk: EditText
    private lateinit var edHargaProduk: EditText
    private lateinit var edDeskripsiProduk: EditText
    private lateinit var edStokProduk: EditText

    private val addProductViewModel: AddProductViewModel by viewModels {
        ViewModelFactory(UserPreferences.getInstance(this.dataStore), ApiConfig)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        edNamaProduk = binding.edProductName
        edDeskripsiProduk = binding.edProductDesc
        edHargaProduk = binding.edProductCost
        edStokProduk = binding.edProductStock

        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnAddProduct.setOnClickListener { addProduct() }

        binding.ivBackForm.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun addProduct() {
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
            addProductViewModel.postProduct(
                imageMultipart,
                title,
                cost,
                description,
                stock
            )
            addProductViewModel.addProductResponse.observe(this) {
                Toast.makeText(this@AddProductActivity, "Berhasil diunggah!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@AddProductActivity, MainActivity::class.java))
                finish()
            }
        } else {
            Toast.makeText(this@AddProductActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddProductActivity,
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
        intent.action = ACTION_GET_CONTENT
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
                binding.ivPreview.setImageBitmap(BitmapFactory.decodeFile(file.path))
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
                val myFile = uriToFile(uri, this@AddProductActivity)
                getFile = myFile
                binding.ivPreview.setImageURI(uri)
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