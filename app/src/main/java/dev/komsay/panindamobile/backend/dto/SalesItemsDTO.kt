package dev.komsay.panindamobile.backend.dto

data class SalesItemsDTO(
    val id: Long? = null,
    val salesId: Long? = null,
    val productId: Long?,
    val productName: String,
    var quantity: Int,
    val unitPrice: Double,
    val subtotal: Double
)
