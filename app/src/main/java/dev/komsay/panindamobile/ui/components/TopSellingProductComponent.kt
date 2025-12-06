package dev.komsay.panindamobile.ui.components

import android.view.*
import android.widget.*
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.ui.data.Product

class TopSellingProductComponent {

    private val view: View
    private val productImage: ImageView
    private val productName: TextView
    private val productPercentage: TextView

    constructor(container: LinearLayout) {
        view = LayoutInflater.from(container.context)
            .inflate(R.layout.component_top_selling_product, container, false)

        container.addView(view)

        productImage = view.findViewById(R.id.topProductImage)
        productName = view.findViewById(R.id.topProductName)
        productPercentage = view.findViewById(R.id.productPercentage)

    }

    fun bind(product: Product, overAllUnitSold: Int) {
        productName.text = product.name
        //productPercentage.text = "${getFormattedPercentage(product.unitSold, overAllUnitSold)}% of the sales for the last 7 days"

        product.imageResId?.let { resourceId ->
            productImage.setImageResource(resourceId)
        }

    }

    private fun getFormattedPercentage(unitSold: Int, overAllUnitSold: Int): String {
        val percentage = (unitSold.toDouble() / overAllUnitSold.toDouble()) * 100

        return "%.2f%%".format(percentage)
    }

}