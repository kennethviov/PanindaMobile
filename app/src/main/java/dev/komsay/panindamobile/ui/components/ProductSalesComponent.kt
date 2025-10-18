package dev.komsay.panindamobile.ui.components

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.ui.data.Product

class ProductSalesComponent {

    private val view: View
    private val productImage: ImageView
    private val productName: TextView
    private val productPrice: TextView
    private val productSold: TextView
    private val productSales: TextView

    constructor(container: LinearLayout) {
        view = LayoutInflater.from(container.context)
            .inflate(R.layout.component_product_sales,
            container, false)

        container.addView(view)

        productImage = view.findViewById(R.id.productImage)
        productName = view.findViewById(R.id.productName)
        productPrice = view.findViewById(R.id.productPrice)
        productSold = view.findViewById(R.id.productSold)
        productSales = view.findViewById(R.id.productSales)

    }

    fun bind(product: Product) {

        productName.text = product.name
        productPrice.text = "₱%.2f".format(product.price)
        productSold.text = product.unitSold.toString()
        productSales.text = calcSales(product.price, product.unitSold)

        product.imageResId?.let { resourceId ->
            productImage.setImageResource(resourceId)
        }
    }

    private fun calcSales(price: Double, unitSold: Int): String {
        val unitSales = unitSold * price
        return "₱%.2f".format(unitSales)
    }
}