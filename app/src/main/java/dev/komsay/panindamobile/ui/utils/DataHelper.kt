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
import dev.komsay.panindamobile.ui.data.Sale
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
    private val _sales = mutableListOf<Sale>()
    val sales: List<Sale> get() = _sales
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
                Product(id = "10", name = "Sprite", price = 15.00, stock = 18, category = "Beverages", unitSold = 40),
                Product(id = "11", name = "Royal", price = 15.00, stock = 10, category = "Beverages", unitSold = 35),
                Product(id = "12", name = "Nova", price = 12.00, stock = 25, category = "Snacks", unitSold = 22),
                Product(id = "13", name = "Roller Coaster", price = 11.00, stock = 30, category = "Snacks", unitSold = 28),
                Product(id = "14", name = "Chippy", price = 10.00, stock = 35, category = "Snacks", unitSold = 32),
                Product(id = "15", name = "Cup Noodles", price = 25.00, stock = 20, category = "Instant Food", unitSold = 18),
                Product(id = "16", name = "SkyFlakes", price = 8.00, stock = 40, category = "Snacks", unitSold = 27),
                Product(id = "17", name = "Pancit Canton", price = 17.00, stock = 22, category = "Instant Food", unitSold = 30),
                Product(id = "18", name = "Energy Drink", price = 30.00, stock = 12, category = "Beverages", unitSold = 16),
                Product(id = "19", name = "Hotdog", price = 6.00, stock = 60, category = "Frozen", unitSold = 45),
                Product(id = "20", name = "Egg", price = 8.00, stock = 50, category = "Produce", unitSold = 38)
                )

            _products.addAll(initialProducts)
            saveProducts()
        }

        // populate sales with initial data and save
        if (_sales.isEmpty()) {
            val initialSales = mutableListOf(
                // === OLDEST ===
                Sale(
                    id = "1001",
                    salesDate = LocalDateTime.now().minusDays(330).toString(),
                    salesItems = mutableListOf(
                        CartItem(_products[1].imageResId, _products[1].name, _products[1].price, 12),
                        CartItem(_products[14].imageResId, _products[14].name, _products[14].price, 8)
                    )
                ),

                Sale(
                    id = "1002",
                    salesDate = LocalDateTime.now().minusDays(200).toString(),
                    salesItems = mutableListOf(
                        CartItem(_products[3].imageResId, _products[3].name, _products[3].price, 3),
                        CartItem(_products[8].imageResId, _products[8].name, _products[8].price, 4)
                    )
                ),

                Sale(
                    id = "1003",
                    salesDate = LocalDateTime.now().minusDays(120).toString(),
                    salesItems = mutableListOf(
                        CartItem(_products[19].imageResId, _products[19].name, _products[19].price, 10)
                    )
                ),

                Sale(
                    id = "1004",
                    salesDate = LocalDateTime.now().minusDays(90).toString(),
                    salesItems = mutableListOf(
                        CartItem(_products[18].imageResId, _products[18].name, _products[18].price, 6)
                    )
                ),

                Sale(
                    id = "1005",
                    salesDate = LocalDateTime.now().minusDays(60).toString(),
                    salesItems = mutableListOf(
                        CartItem(_products[4].imageResId, _products[4].name, _products[4].price, 5),
                        CartItem(_products[17].imageResId, _products[17].name, _products[17].price, 4)
                    )
                ),

                Sale(
                    id = "1006",
                    salesDate = LocalDateTime.now().minusDays(45).toString(),
                    salesItems = mutableListOf(
                        CartItem(_products[16].imageResId, _products[16].name, _products[16].price, 12)
                    )
                ),

                Sale(
                    id = "1007",
                    salesDate = LocalDateTime.now().minusDays(30).toString(),
                    salesItems = mutableListOf(
                        CartItem(_products[15].imageResId, _products[15].name, _products[15].price, 3),
                        CartItem(_products[0].imageResId, _products[0].name, _products[0].price, 5)
                    )
                ),

                Sale(
                    id = "1008",
                    salesDate = LocalDateTime.now().minusDays(20).toString(),
                    salesItems = mutableListOf(
                        CartItem(_products[14].imageResId, _products[14].name, _products[14].price, 10)
                    )
                ),

                Sale(
                    id = "1009",
                    salesDate = LocalDateTime.now().minusDays(14).toString(),
                    salesItems = mutableListOf(
                        CartItem(_products[3].imageResId, _products[3].name, _products[3].price, 6),
                        CartItem(_products[13].imageResId, _products[13].name, _products[13].price, 7)
                    )
                ),

                Sale(
                    id = "1010",
                    salesDate = LocalDateTime.now().minusDays(10).toString(),
                    salesItems = mutableListOf(
                        CartItem(_products[11].imageResId, _products[11].name, _products[11].price, 4),
                        CartItem(_products[2].imageResId, _products[2].name, _products[2].price, 3)
                    )
                ),

                Sale(
                    id = "1011",
                    salesDate = LocalDateTime.now().minusDays(4).toString(),
                    salesItems = mutableListOf(
                        CartItem(_products[9].imageResId, _products[9].name, _products[9].price, 3),
                        CartItem(_products[10].imageResId, _products[10].name, _products[10].price, 5)
                    )
                ),

                Sale(
                    id = "1012",
                    salesDate = LocalDateTime.now().minusDays(3).toString(),
                    salesItems = mutableListOf(
                        CartItem(_products[8].imageResId, _products[8].name, _products[8].price, 6)
                    )
                ),

                Sale(
                    id = "1013",
                    salesDate = LocalDateTime.now().minusHours(5).toString(),
                    salesItems = mutableListOf(
                        CartItem(_products[4].imageResId, _products[4].name, _products[4].price, 5),
                        CartItem(_products[5].imageResId, _products[5].name, _products[5].price, 4)
                    )
                ),

                Sale(
                    id = "1014",
                    salesDate = LocalDateTime.now().minusMinutes(30).toString(),
                    salesItems = mutableListOf(
                        CartItem(_products[6].imageResId, _products[6].name, _products[6].price, 2),
                        CartItem(_products[7].imageResId, _products[7].name, _products[7].price, 9)
                    )
                ),

                // === NEWEST ===
                Sale(
                    id = "1015",
                    salesDate = LocalDateTime.now().toString(),
                    salesItems = mutableListOf(
                        CartItem(_products[0].imageResId, _products[0].name, _products[0].price, 9),
                        CartItem(_products[1].imageResId, _products[1].name, _products[1].price, 7),
                        CartItem(_products[2].imageResId, _products[2].name, _products[2].price, 5),
                        CartItem(_products[3].imageResId, _products[3].name, _products[3].price, 6)
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

    fun getAllSales(): List<Sale> {
        Log.i("DataHelper.getAllSales", _sales.reversed().toString())
        return _sales.reversed()
    }

    fun addSale(sale: Sale): Sale {
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

    fun getSale(id: String): Sale? = _sales.find { it.id == id }


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
            val saleType = object : TypeToken<MutableList<Sale>>() {}.type
            val loadedSales: MutableList<Sale> = gson.fromJson(salesJson, saleType)
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