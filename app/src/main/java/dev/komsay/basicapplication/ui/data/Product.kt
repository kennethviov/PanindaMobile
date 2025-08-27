package dev.komsay.basicapplication.ui.data

data class Product(
    val id: String,
    var name: String,
    var price: Double,
    var stock: Int,
    var category: String,

    val imageResId: Int? = null, // subject to change
    // or
    val imageURL: String? = null
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