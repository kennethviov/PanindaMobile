package dev.komsay.panindamobile.ui.data

import dev.komsay.panindamobile.R

data class Product(
    var id: String,
    var name: String,
    var price: Double,
    var stock: Int,
    var category: String,
    var unitSold: Int = 0,

    var imageResId: Int? = R.drawable.img_placeholder, // subject to change
    // or
    var imageURL: String? = null
    ) {
    fun getFormattedPrice(): String {
        return "₱%.2f".format(price)
    }

    fun isInStock(): Boolean {
        return stock > 0
    }

    fun isQuantityAvailable(quantity: Int): Boolean {
        return quantity <= stock && quantity > 0
    }

    fun deductStock(quantity: Int): Boolean {
        return if (isQuantityAvailable(quantity)) {
            stock -= quantity
            true
        } else {
            false
        }
    }
}