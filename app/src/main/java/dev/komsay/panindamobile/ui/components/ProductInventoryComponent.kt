package dev.komsay.panindamobile.ui.components

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import dev.komsay.panindamobile.AddProduct
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.ui.data.Product

class ProductInventoryComponent {

    private val view: View
    private val productImage: ImageView
    private val productName: TextView
    private val productPrice: TextView
    private val productStock: TextView
    private val category : String
    private val updateBtn : Button


    constructor(container: LinearLayout) {
        view = LayoutInflater.from(container.context)
            .inflate(R.layout.component_product_inventory, container, false)

        container.addView(view)

        productImage = view.findViewById(R.id.productImage)
        productName = view.findViewById(R.id.productName)
        productPrice = view.findViewById(R.id.productPrice)
        productStock = view.findViewById(R.id.productStock)
        category = ""
        updateBtn = view.findViewById(R.id.updateButton)

    }

    fun bind(product: Product) {

        productName.text = product.name
        productPrice.text = product.price.toString()
        productStock.text = product.stock.toString()

        product.imageResId?.let { resourceId ->
            productImage.setImageResource(resourceId)
        }

    }

}