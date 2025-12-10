package dev.komsay.panindamobile.backend.dto

import java.time.LocalDateTime

data class SalesDTO(
    val id: Long? = null,
    val salesDate: String,
    val totalPrice: Double,
    val items: List<SalesItemsDTO>

)
