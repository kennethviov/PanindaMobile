package dev.komsay.panindamobile.backend.dto

import java.util.UUID

data class UpdatePasswordDTO (
    val username: String,
    val newPassword: String
)