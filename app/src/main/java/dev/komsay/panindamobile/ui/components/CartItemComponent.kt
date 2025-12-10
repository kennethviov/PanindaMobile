package dev.komsay.panindamobile.ui.components

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.backend.dto.SalesItemsDTO
import dev.komsay.panindamobile.ui.data.CartItem
import dev.komsay.panindamobile.ui.data.Product

class CartItemComponent {
    private val view: View
    private val container: LinearLayout
    private val productName: TextView
    private val productPrice: TextView
    private val productTotal: TextView
    private val productQuantity: TextView
//    private val incBtn: Button
//    private val decBtn: Button

    constructor(container: LinearLayout) {
        this.container = container

        view = LayoutInflater.from(container.context).inflate(R.layout.component_cart_item, container, false)

        productName = view.findViewById(R.id.productName)
        productPrice = view.findViewById(R.id.productPrice)
        productTotal = view.findViewById(R.id.productTotal)
        productQuantity = view.findViewById(R.id.productQuantity)
//        incBtn = view.findViewById(R.id.btn_inc)
//        decBtn = view.findViewById(R.id.btn_dec)

        container.addView(view)
    }

    // TODO: handle incButton and decButton

    fun bind(item: SalesItemsDTO) {

        productName.text = item.productName
        productPrice.text = formatPrice(item.unitPrice)
        productTotal.text = formatPrice(item.subtotal)
        productQuantity.text = item.quantity.toString()
    }

    @SuppressLint("DefaultLocale")
    private fun formatPrice(price: Double): String {
        return String.format("₱%.2f", price)
    }
}