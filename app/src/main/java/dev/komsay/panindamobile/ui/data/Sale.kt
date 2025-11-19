package dev.komsay.panindamobile.ui.data

data class Sale(
    var id: String,
    val salesDate: String,

    val salesItems: MutableList<CartItem>
) {
    private var total: Double = 0.0


    init {
        total = salesItems.sumOf { it.productPrice * it.productQuantity }
    }

    fun getFormattedTotal(total: Double): String {
        return "₱%.2f".format(total)
    }

    fun salesTotal(total: Double) {
        this.total = total
    }

    fun salesTotal(): Double {
        return total
    }
}
