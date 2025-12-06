package dev.komsay.panindamobile.backend.dto

import java.util.UUID

data class CategoryDTO(
    val categoryId: UUID,
    val categoryName: String,
    val products: List<ProductsDTO>
)
