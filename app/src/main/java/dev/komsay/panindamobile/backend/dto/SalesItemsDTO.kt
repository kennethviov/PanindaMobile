package dev.komsay.panindamobile.backend.dto

data class SalesItemsDTO(
    val id: Long,
    val salesId: Long,
    val productId: Long,
    val quantity: Int,
    val unitPrice: Double,
    val subTotal: Double
)
