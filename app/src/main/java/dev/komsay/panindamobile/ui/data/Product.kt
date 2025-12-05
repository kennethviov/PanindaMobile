package dev.komsay.panindamobile.ui.data

import dev.komsay.panindamobile.R
import java.util.UUID

data class Product(
    val id: Long,
    val name: String,
    val price: Double,
    var stock: Int,
    val category: UUID?,
    val categoryName: String?,

    var imageResId: Int? = R.drawable.img_placeholder, // subject to change
    // or
    var imageURL: String? = null,

    val imageName: String?,     // optional
    val imageType: String?      // optional (e.g., "image/jpeg")
    ) {
    fun getFormattedPrice(price: Double): String {
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