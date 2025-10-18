package dev.komsay.panindamobile.ui.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.ui.data.Product
import androidx.core.content.edit
import com.google.gson.reflect.TypeToken


/*
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

class DataHelper(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("products_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val _products = mutableListOf<Product>()
    val product: List<Product> get() = _products

    private var id: Int = 0

    init {
        val products = mutableListOf<Product>(
            Product(
                id = "1",
                name = "Piattos",
                price = 10.50,
                stock = 20,
                category = "Snacks",
                unitSold = 20,
                imageResId = R.drawable.img_piattos
            ),
            Product(
                id = "2",
                name = "Banana",
                price = 15.00,
                stock = 15,
                category = "Snacks",
                unitSold = 10
            ),
            Product(
                id = "3",
                name = "Apple",
                price = 10.00,
                stock = 12,
                category = "Snacks",
                unitSold = 10
            ),
            Product(
                id = "4",
                name = "Mango",
                price = 25.00,
                stock = 8,
                category = "Snacks",
                unitSold = 12
            ),
            Product(
                id = "5",
                name = "Orange",
                price = 15.00,
                stock = 10,
                category = "Snacks",
                unitSold = 8
            ),
            Product(
                id = "6",
                name = "Coffee",
                price = 20.00,
                stock = 5,
                category = "Beverages",
                unitSold = 20
            ),
            Product(
                id = "7",
                name = "Tea",
                price = 15.00,
                stock = 10,
                category = "Beverages",
                unitSold = 10
            ),
            Product(
                id = "8",
                name = "Water",
                price = 10.00,
                stock = 20,
                category = "Beverages",
                unitSold = 50
            ),
            Product(
                id = "9",
                name = "Coke",
                price = 15.00,
                stock = 15,
                category = "Beverages",
                unitSold = 55
            ),
        )

        for (product in products) {
            this._products.add(product)
        }
    }

    public fun getAllProducts(): List<Product> {
        return _products
    }

    public fun getAllCategories(): List<String> {
        val categories = mutableListOf<String>()

        for (product in _products) {
            if (product.category !in categories) {
                categories.add(product.category)
            }
        }

        return categories
    }

    public fun addProduct(product: Product): Product? {
        if (!_products.contains(product)) {
            product.id = (id + 1).toString()
            _products.add(product)
            save()
            return _products.last()
        } else {
            return null
        }
    }

    public fun getProduct(id: Int): Product? {
        val id = id.toString()
        for (product in _products) {
            if (product.id == id) {
                return product
            }
        }
        return null
    }

    public fun updateProduct(oldProduct: Product,updatedProduct: Product) {
        for (product in _products) {
            if (product.id == oldProduct.id) {
                product.name = updatedProduct.name
                product.price = updatedProduct.price
                product.stock = updatedProduct.stock
                product.category = updatedProduct.category
                product.imageResId = product.imageResId
                save()
            }
        }
    }

    public fun deleteProduct(product: Product) {
        _products.removeAll { it.id == product.id }
        save()
    }

    private fun save() {
        val json = gson.toJson(_products)
        prefs.edit { putString("products_json", json) }
    }

    private fun load() {
        val json = prefs.getString("products_json", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<Product>>() {}.type
            val loadedProducts: MutableList<Product> = gson.fromJson(json, type)
            _products.clear()
            _products.addAll(loadedProducts)
        }
    }
}