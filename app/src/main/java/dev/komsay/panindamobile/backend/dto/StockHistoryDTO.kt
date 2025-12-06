package dev.komsay.panindamobile.backend.dto

import java.time.LocalDateTime

data class StockHistoryDTO(
    val id: Long,
    val quantityChanged: Int,
    val updated: LocalDateTime,

    val productId: Long,
    val productName: String,
    val categoryName: String,

    val imageName: String,
    val imageType: String,
    val imageData: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StockHistoryDTO

        if (id != other.id) return false
        if (quantityChanged != other.quantityChanged) return false
        if (productId != other.productId) return false
        if (updated != other.updated) return false
        if (productName != other.productName) return false
        if (categoryName != other.categoryName) return false
        if (imageName != other.imageName) return false
        if (imageType != other.imageType) return false
        if (!imageData.contentEquals(other.imageData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + quantityChanged
        result = 31 * result + productId.hashCode()
        result = 31 * result + updated.hashCode()
        result = 31 * result + productName.hashCode()
        result = 31 * result + categoryName.hashCode()
        result = 31 * result + imageName.hashCode()
        result = 31 * result + imageType.hashCode()
        result = 31 * result + imageData.contentHashCode()
        return result
    }
}
