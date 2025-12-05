// dev.komsay.panindamobile.dto.ProductDTO.kt
package dev.komsay.panindamobile.backend.dto

import java.util.UUID

data class ProductsDTO(
    val id: Long? = null,
    val name: String,
    val price: Double,
    val stocks: Int,

    val category: UUID?,
    val categoryName: String?,

    val imageUrl: String? = null, // optional (e.g., "image/jpeg")
    val imageName: String? = null
    // Remove imageURL — it's not sent by backend!
) {
    fun getFormattedPrice(): String {
        return "₱%.2f".format(price)
    }

    fun isInStock(): Boolean {
        return stocks > 0
    }

    fun isQuantityAvailable(quantity: Int): Boolean {
        return quantity <= stocks && quantity > 0
    }
}