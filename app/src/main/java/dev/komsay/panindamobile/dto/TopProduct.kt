package dev.komsay.panindamobile.dto

data class TopProduct(
    val id: Long,
    val name: String,
    val qty: Long,
    val revenue: Double,
    val sales: Long,
    val imageURL: String
)
