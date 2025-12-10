package dev.komsay.panindamobile.backend.dto

import java.util.UUID

data class UserDTO(
    val id: UUID?,
    val username: String,
    val password: String,
    val createdAt: String?,
    val updatedAt: String?,
    val products: List<ProductsDTO>? = null
)
