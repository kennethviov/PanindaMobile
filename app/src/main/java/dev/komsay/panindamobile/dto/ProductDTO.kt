package dev.komsay.panindamobile.dto

import java.net.URL
import java.util.UUID

data class ProductDTO(
    val id: Long,
    val name: String,
    val price: Double,
    val stock: Int,
    val category: UUID,
    val categoryName: String,
    val imageURL: String,
    val imageName: String
)
