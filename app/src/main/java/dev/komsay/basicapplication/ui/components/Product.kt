package dev.komsay.basicapplication.ui.components

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    var stock: Int,
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

    fun getStockText(): String {
        return "Stock: $stock"
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
