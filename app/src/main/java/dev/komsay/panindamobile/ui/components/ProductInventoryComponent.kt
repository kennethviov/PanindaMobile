package dev.komsay.panindamobile.ui.components

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import dev.komsay.panindamobile.ui.fragments.AddProduct
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.ui.data.Product
import android.content.Context

class ProductInventoryComponent {

    private val view: View
    private val container: LinearLayout
    private val context: Context
    private val productImage: ImageView
    private val productName: TextView
    private val productPrice: TextView
    private val productStock: TextView
    private val productTotal: TextView
    private var category : String


    constructor(container: LinearLayout, context: Context) {
        this.context = context

        view = LayoutInflater.from(container.context)
            .inflate(R.layout.component_product_inventory, container, false)

        this.container = container

        productImage = view.findViewById(R.id.productImage)
        productName = view.findViewById(R.id.productName)
        productPrice = view.findViewById(R.id.productPrice)
        productStock = view.findViewById(R.id.productStock)
        productTotal = view.findViewById(R.id.productTotal)
        category = ""

    }

    fun bind(product: Product, invoker: String?) {

        if (invoker == "sales") {
            productTotal.visibility = View.VISIBLE
            productTotal.text = product.getFormattedPrice(product.stock * product.price)
        } else {
            view.setOnLongClickListener {
                val dialog = AddProduct(context)
                dialog.show(product)
                true
            }
        }

        productName.text = product.name
        productPrice.text = product.getFormattedPrice(product.price)
        productStock.text = product.stock.toString()
        category = product.category

        product.imageResId?.let { resourceId ->
            productImage.setImageResource(resourceId)
        }

        container.addView(view)
    }

    fun getView(): View { return view }

}