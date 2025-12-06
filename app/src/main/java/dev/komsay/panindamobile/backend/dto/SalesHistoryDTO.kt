package dev.komsay.panindamobile.backend.dto

import java.time.LocalDateTime

data class SalesHistoryDTO(
    val id: Long,
    val salesId: Long,
    val salesDate: LocalDateTime
)
