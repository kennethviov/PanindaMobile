// dev.komsay.panindamobile.dto.ProductDTO.kt
package dev.komsay.panindamobile.dto

import java.util.UUID

data class ProductDTO(
    val id: Long,
    val name: String,
    val price: Double,
    val stock: Int,
    val category: UUID?,
    val categoryName: String?,
    val imageName: String?,     // optional
    val imageType: String?      // optional (e.g., "image/jpeg")
    // Remove imageURL — it's not sent by backend!
)