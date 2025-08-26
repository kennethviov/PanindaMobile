package dev.komsay.panindamobile.ui.components

import android.view.*
import android.widget.*
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.ui.data.ProductSales

class TopSellingProductComponent {

    private val view: View
    private val productImage: ImageView
    private val productName: TextView
    private val productPercentage: TextView

    constructor(container: LinearLayout) {
        view = LayoutInflater.from(container.context)
            .inflate(R.layout.top_selling_product_component, container, false)

        container.addView(view)

        productImage = view.findViewById(R.id.topProductImage)
        productName = view.findViewById(R.id.topProductName)
        productPercentage = view.findViewById(R.id.productPercentage)

    }

    fun bind(product: ProductSales) {
        productName.text = product.name
        productPercentage.text = "${product.getFormattedPercentage()}% of the sales for the last 7 days"

        product.imageResId?.let { resourceId ->
            productImage.setImageResource(resourceId)
        }

    }

}