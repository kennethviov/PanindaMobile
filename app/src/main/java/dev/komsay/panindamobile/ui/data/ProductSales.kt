package dev.komsay.panindamobile.ui.data

data class ProductSales(
    val id: String,
    val name: String,
    val stockSold: Int,
    val totalSales: Double,
    val imageResId: Int? = null, // subject to change
    // or
    val imageURL: String? = null,

    val overAllStocksSold: Int
    ) {
    fun getFormattedPercentage(): String {
        val percentage = (stockSold.toDouble() / overAllStocksSold.toDouble()) * 100

        return "%.2f%%".format(percentage)
    }
}
