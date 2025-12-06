package dev.komsay.panindamobile.backend.dto

data class LoginResponseDTO(
    val token: String,
    val username: String,
    val expiresIn: Long
)
