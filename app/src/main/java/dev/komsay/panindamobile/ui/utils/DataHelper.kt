package dev.komsay.panindamobile.ui.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.ui.data.Product
import androidx.core.content.edit
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import dev.komsay.panindamobile.ui.data.CartItem
import dev.komsay.panindamobile.ui.data.Sales
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
*
*
*
*
*
*   FOR DATA TEST ONLY
*
*
*
*
* */

@RequiresApi(Build.VERSION_CODES.O)
class DataHelper(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("products_prefs", Context.MODE_PRIVATE)
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    // -- Products Data
    private val _products = mutableListOf<Product>()
    val product: List<Product> get() = _products
    private var productId: Int = 0

    // -- Sales Data
    private val _sales = mutableListOf<Sales>()
    val sales: List<Sales> get() = _sales
    private var salesId: Int = 0

    // --- rofile (Sample user)
    data class Profile(
        var id: String = "1",
        var name: String = "admin",
        var password: String = "admin",

        /**
         * imageUri: when users pick image from gallery
         * imageResId: default picture
         *
         * imageUri takes precedence when present
         */
        var imageUri: String? = null,
        var imageResId: Int = R.drawable.img_placeholder_photo
    )

    private var _profile: Profile? = null
    val profile: Profile?
        get() = _profile

    init {
        // remove this ↓ for persistent data
        prefs.edit().clear().apply()

        // Load from SharedPreferences first
        load()

        // populate products with initial data and save
        if (_products.isEmpty()) {
            val initialProducts = mutableListOf(
                Product(id = "1", name = "Piattos", price = 10.50, stock = 20, category = "Snacks", unitSold = 20, imageResId = R.drawable.img_piattos),
                Product(id = "2", name = "Banana", price = 15.00, stock = 15, category = "Snacks", unitSold = 10),
                Product(id = "3", name = "Apple", price = 10.00, stock = 12, category = "Snacks", unitSold = 10),
                Product(id = "4", name = "Mango", price = 25.00, stock = 8, category = "Snacks", unitSold = 12),
                Product(id = "5", name = "Orange", price = 15.00, stock = 10, category = "Snacks", unitSold = 8),
                Product(id = "6", name = "Coffee", price = 20.00, stock = 5, category = "Beverages", unitSold = 20),
                Product(id = "7", name = "Tea", price = 15.00, stock = 10, category = "Beverages", unitSold = 10),
                Product(id = "8", name = "Water", price = 10.00, stock = 20, category = "Beverages", unitSold = 50),
                Product(id = "9", name = "Coke", price = 15.00, stock = 15, category = "Beverages", unitSold = 55),
            )
            _products.addAll(initialProducts)
            saveProducts()
        }

        // populate sales with initial data and save
        if (_sales.isEmpty()) {
            val initialSales = mutableListOf(
                Sales(
                    id = "1001",
                    salesDate = LocalDateTime.now().minusDays(1).toString(),
                    salesItems = mutableListOf(
                        CartItem (
                            imageResId = _products[0].imageResId,
                            productName = _products[0].name,
                            productPrice = _products[0].price,
                            productQuantity = 9
                        ),
                        CartItem (
                            imageResId = _products[1].imageResId,
                            productName = _products[1].name,
                            productPrice = _products[1].price,
                            productQuantity = 7
                        ),
                        CartItem (
                            imageResId = _products[2].imageResId,
                            productName = _products[2].name,
                            productPrice = _products[2].price,
                            productQuantity = 5
                        ),
                        CartItem (
                            imageResId = _products[3].imageResId,
                            productName = _products[3].name,
                            productPrice = _products[3].price,
                            productQuantity = 6
                        )
                    )
                ),
                Sales(
                    id = "1002",
                    salesDate = LocalDateTime.now().minusHours(5).toString(),
                    salesItems = mutableListOf(
                        CartItem (
                            imageResId = _products[4].imageResId,
                            productName = _products[4].name,
                            productPrice = _products[4].price,
                            productQuantity = 5
                        ),
                        CartItem (
                            imageResId = _products[5].imageResId,
                            productName = _products[5].name,
                            productPrice = _products[5].price,
                            productQuantity = 4
                        )
                    )
                ),
                Sales(
                    id = "1003",
                    salesDate = LocalDateTime.now().minusMinutes(30).toString(),
                    salesItems = mutableListOf(
                        CartItem (
                            imageResId = _products[6].imageResId,
                            productName = _products[6].name,
                            productPrice = _products[6].price,
                            productQuantity = 2
                        ),
                        CartItem (
                            imageResId = _products[7].imageResId,
                            productName = _products[7].name,
                            productPrice = _products[7].price,
                            productQuantity = 9
                        )
                    )
                ),
                Sales(
                    id = "1004",
                    salesDate = LocalDateTime.now().minusDays(3).toString(),
                    salesItems = mutableListOf(
                        CartItem (
                            imageResId = _products[8].imageResId,
                            productName = _products[8].name,
                            productPrice = _products[8].price,
                            productQuantity = 6
                        )
                    )
                )
            )
            _sales.addAll(initialSales)
            saveSales()
        }

        // if profile is null, create a default mock profile for testing
        if (_profile == null) {
            _profile = Profile(
                id = "1",
                name = "admin",
                password = "admin",
                imageResId = R.drawable.img_placeholder_photo
            )
            saveProfile()
        }

        // Set latest ID for new entries
        productId = _products.mapNotNull { it.id.toIntOrNull() }.maxOrNull() ?: 0
        salesId = _sales.mapNotNull { it.id.toIntOrNull() }.maxOrNull() ?: 0
    }


    // +-------------------+
    // |  Product Methods  |
    // +-------------------+

    fun getAllProducts(): List<Product> = _products

    fun getAllCategories(): List<String> = _products.map { it.category }.distinct()

    fun addProduct(product: Product): Product {
        productId++
        val newProduct = product.copy(id = productId.toString())
        _products.add(newProduct)
        saveProducts()
        return newProduct
    }

    fun getProduct(id: String): Product? = _products.find { it.id == id }

    fun updateProduct(updatedProduct: Product) {
        val index = _products.indexOfFirst { it.id == updatedProduct.id }
        if (index != -1) {
            _products[index] = updatedProduct
            saveProducts()
        }
    }

    fun deleteProduct(product: Product) {
        _products.removeAll { it.id == product.id }
        saveProducts()
    }


    // +-----------------+
    // |  Sales Methods  |
    // +-----------------+

    fun getAllSales(): List<Sales> {
        Log.i("DataHelper.getAllSales", _sales.reversed().toString())
        return _sales.reversed()
    }

    fun addSale(sale: Sales): Sales {
        salesId++
        val newSale = sale
        Log.i("DataHelper.addSell before", newSale.toString())
        // log println(newSale)
        newSale.id = salesId.toString()
        _sales.add(newSale)
        saveSales()
        Log.i("DataHelper.addSell after", (_sales.find { it.id == newSale.id}).toString() )
        Log.i("DataHelper.addSale all", _sales.reversed().toString())
        return newSale
    }

    fun getSale(id: String): Sales? = _sales.find { it.id == id }


    // +-------------------+
    // |  Profile Methods  |
    // +-------------------+

    // saving and retrieving profile

    fun saveProfile(profile: Profile) {
        _profile = profile
        saveProfile()
    }

    // updating profile pics
    fun updateProfileImageUri(uriString: String) {
        if (_profile == null) _profile = Profile()
        _profile?.imageUri = uriString
        saveProfile()
    }

    fun updateProfileImageResId(resId: Int) {
        if (_profile == null) _profile = Profile()
        _profile?.imageResId = resId
        saveProfile()
    }

    // clear profile picture
    fun clearProfileImage() {
        _profile?.imageUri = null
        saveProfile()
    }


    // +-------------------------------------------------+
    // |  Persistence Helpers SharedPreferences Methods  |
    // +-------------------------------------------------+
    private fun saveProducts() {
        val json = gson.toJson(_products)
        prefs.edit { putString("products_json", json) }
    }

    private fun saveSales() {
        val json = gson.toJson(_sales)
        prefs.edit { putString("sales_json", json) }
    }

    private fun saveProfile() {
        val json = gson.toJson(_profile)
        prefs.edit { putString("profile_json", json) }
    }

    private fun load() {
        // Load Products
        val productsJson = prefs.getString("products_json", null)
        if (productsJson != null) {
            val productType = object : TypeToken<MutableList<Product>>() {}.type
            val loadedProducts: MutableList<Product> = gson.fromJson(productsJson, productType)
            _products.clear()
            _products.addAll(loadedProducts)
        }

        // Load Sales
        val salesJson = prefs.getString("sales_json", null)
        if (salesJson != null) {
            val saleType = object : TypeToken<MutableList<Sales>>() {}.type
            val loadedSales: MutableList<Sales> = gson.fromJson(salesJson, saleType)
            _sales.clear()
            _sales.addAll(loadedSales)
        }

        Log.i("DataHelper.load", _sales.toString())
    }


    // +-------------------------+
    // |  LocalDateTime adapter  |
    // +-------------------------+
    inner class LocalDateTimeAdapter : JsonSerializer<LocalDateTime>,
        JsonDeserializer<LocalDateTime> {
        private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

        override fun serialize(
            src: LocalDateTime?, typeOfSrc: Type?, context: JsonSerializationContext?
        ): JsonElement {
            return JsonPrimitive(src?.format(formatter))
        }

        override fun deserialize(
            json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?
        ): LocalDateTime {
            return LocalDateTime.parse(json?.asString, formatter)
        }
    }
}