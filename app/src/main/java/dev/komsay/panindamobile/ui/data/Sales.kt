package dev.komsay.panindamobile.ui.data

data class Sales(
    val id: String,
    val salesDate: String,
    val salesTotal: Double,

    val salesItems: List<Product>
) {
    fun getFormattedTotal(total: Double): String {
        return "₱%.2f".format(total)
    }
}
