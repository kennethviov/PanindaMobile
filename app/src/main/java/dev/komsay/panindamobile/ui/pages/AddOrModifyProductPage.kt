package dev.komsay.panindamobile.ui.pages

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.backend.dto.ProductsDTO
import dev.komsay.panindamobile.backend.network.RetrofitClient
import dev.komsay.panindamobile.backend.service.SharedPrefManager
import dev.komsay.panindamobile.databinding.ActivityAddOrModifyProductPageBinding
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddOrModifyProductPage() : AppCompatActivity() {

    private lateinit var binding: ActivityAddOrModifyProductPageBinding

    private var productId: Long = 0L
    private var selectedImageUri: Uri? = null
    private lateinit var productImage: ImageView
    private lateinit var btnBack: ImageButton
    private lateinit var btnDelete: ImageButton
    private lateinit var productName: EditText
    private lateinit var productPrice: EditText
    private lateinit var txtStock: TextView
    private lateinit var productStock: EditText
    private lateinit var productCategory: EditText
    private lateinit var btnSave: Button

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            productImage.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddOrModifyProductPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        productImage = binding.productImage
        btnBack = binding.btnBack
        btnDelete = binding.btnDelete
        productName = binding.productName
        productPrice = binding.productPrice
        txtStock = binding.txtStock
        productStock = binding.productStock
        productCategory = binding.productCategory
        btnSave = binding.btnSave

        // Determine if adding or modifying
        productId = intent.getLongExtra("PRODUCT_ID", 0L)

        if (productId != 0L) {
            // --- MODIFY MODE ---
            title = "Edit Product"
            btnDelete.visibility = View.VISIBLE
            getProduct(productId) // Load existing product data

            // Set click listeners for modify mode
            btnSave.text = "Update"
            btnSave.setOnClickListener {
                if (validateForm()) {
                    if (selectedImageUri != null) {
                        updateProductWithImage(productId)
                    } else {
                        updateProduct(productId)
                    }
                }
            }
            btnDelete.setOnClickListener {
                showDeleteConfirmationDialog(productId)
            }
        } else {
            // --- ADD MODE ---
            title = "Add Product"
            btnDelete.visibility = View.GONE // Hide delete button

            // Set click listeners for add mode
            btnSave.text = "Save"
            btnSave.setOnClickListener {
                if (validateForm()) {
                    if (selectedImageUri != null) {
                        saveProduct()
                    } else {
                        saveProductWithoutImage()
                    }
                }
            }
        }

        // Common click listeners
        btnBack.setOnClickListener { goBack() }
        productImage.setOnClickListener { selectImage() }
    }

    private fun goBack() {
        finish()
    }

    private fun getProduct(productId: Long) {
        val api = RetrofitClient.getProductsApi(this)

        lifecycleScope.launch {
            try {
                val product = api.getProduct(productId)

                productName.setText(product.name)
                productPrice.setText(product.price.toString())
                productStock.setText(product.stocks.toString())
                productCategory.setText(product.categoryName)

                product.imageUrl?.let {
                    val imageUrl = "http://10.0.2.2:8080${product.imageUrl}"
                    val token = SharedPrefManager.getToken(this@AddOrModifyProductPage)

                    val glideUrl = GlideUrl(
                        imageUrl,
                        LazyHeaders.Builder()
                            .addHeader("Authorization", "Bearer $token")
                            .build()
                    )

                    Glide.with(this@AddOrModifyProductPage)
                        .load(glideUrl)
                        .placeholder(R.drawable.img_placeholder)
                        .into(productImage)
                }

            } catch (e: Exception) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Error loading product: ${e.message}",
                    Snackbar.LENGTH_LONG
                ).show()

                Log.e("Edit Product", "Error loading product", e)
            }
        }
    }

    private fun saveProduct() {
        val api = RetrofitClient.getProductsApi(this)

        lifecycleScope.launch {
            try {
                val product = createProductDTO()

                val gson = Gson()
                val productJson = gson.toJson(product)
                val productRequestBody = productJson.toRequestBody(
                    "application/json".toMediaTypeOrNull()
                )

                val imageFile = getFileFromUri(selectedImageUri!!)
                val imageRequestBody = imageFile.asRequestBody(
                    "image/jpeg".toMediaTypeOrNull()
                )
                val imagePart = MultipartBody.Part.createFormData(
                    "image",
                    imageFile.name,
                    imageRequestBody
                )

                api.addProductWithImage(productRequestBody, imagePart)

                finish()

            } catch (e: Exception) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Error: ${e.message}",
                    Snackbar.LENGTH_LONG
                ).show()

                Log.e("Add Product", "Error", e)
            }
        }
    }

    private fun saveProductWithoutImage() {
        val api = RetrofitClient.getProductsApi(this)
        val product = createProductDTO()

        lifecycleScope.launch {
            try {
                api.addProduct(product)

                setResult(RESULT_OK)
                finish()
            } catch (e: Exception) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Error: ${e.message}",
                    Snackbar.LENGTH_LONG
                ).show()

                Log.e("Add Product", "Error", e)
            }
        }
    }

    private fun updateProduct(productId: Long) {
        val api = RetrofitClient.getProductsApi(this)

        val updatedProduct = createProductDTO()
        lifecycleScope.launch {
            try {
                api.updateProduct(productId, updatedProduct)

                setResult(RESULT_OK)
                finish()
            } catch (e: Exception) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Error: ${e.message}",
                    Snackbar.LENGTH_LONG
                ).show()

                Log.e("Update Product", "Error", e)
            }
        }
    }

    private fun updateProductWithImage(productId: Long) {
        val api = RetrofitClient.getProductsApi(this)

        lifecycleScope.launch {
            try {
                val updatedProduct = createProductDTO()

                val imageFile = getFileFromUri(selectedImageUri!!)
                val imageRequestBody = imageFile.asRequestBody(
                    "image/jpeg".toMediaTypeOrNull()
                )
                val imagePart = MultipartBody.Part.createFormData(
                    "image",
                    imageFile.name,
                    imageRequestBody
                )

                api.updateProductImage(productId, imagePart)
                api.updateProduct(productId, updatedProduct)

                finish()
            } catch (e: Exception) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Error: ${e.message}",
                    Snackbar.LENGTH_LONG
                ).show()

                Log.e("Update Product", "Error", e)
            }
        }
    }

    private fun deleteProduct(productId: Long) {
        val api = RetrofitClient.getProductsApi(this)

        lifecycleScope.launch {
            try {
                api.deleteProduct(productId)

                setResult(RESULT_OK)
                finish()
            } catch (e: Exception) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Error: ${e.message}",
                    Snackbar.LENGTH_LONG
                ).show()

                Log.e("Delete Product", "Error", e)
            }
        }
    }

    private fun selectImage() {
        pickImageLauncher.launch("image/*")
    }

    private fun validateForm(): Boolean {
        if (productName.text.isBlank()) {
            productName.error = "Product name is required"
            productName.requestFocus()
            return false
        }
        if (productPrice.text.isBlank()) {
            productPrice.error = "Product price is required"
            productPrice.requestFocus()
            return false
        }
        if (productStock.text.isBlank()) {
            productStock.error = "Product stock is required"
            productStock.requestFocus()
            return false
        }

        return true
    }

    private fun createProductDTO() : ProductsDTO {
        return ProductsDTO(
            name = productName.text.toString(),
            price = productPrice.text.toString().toDouble(),
            stocks = productStock.text.toString().toInt(),
            category = null,
            categoryName = productCategory.text.toString(),
        )
    }

    private fun getFileFromUri(uri: Uri): File  {
        val contentResolver = contentResolver
        val file = File(cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")

        contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return file
    }

    private fun showDeleteConfirmationDialog(productId: Long) {
        AlertDialog.Builder(this)
            .setTitle("Delete Product")
            .setMessage("Are you sure you want to delete this product? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                deleteProduct(productId)
            }
            .setNegativeButton("Cancel", null)
            .setIcon(R.drawable.ic_exclamation_mark)
            .show()
    }
}
