package dev.komsay.panindamobile.backend.dto

data class TopProductsDTO(
    val id: Long,
    val name: String,
    val qty: Long,
    val revenue: Double,
    val sales: Long,
    val imageURL: String
)
