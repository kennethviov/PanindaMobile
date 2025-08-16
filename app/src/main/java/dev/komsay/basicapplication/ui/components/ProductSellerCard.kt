package dev.komsay.basicapplication.ui.components

import android.view.View
import android.widget.*
import dev.komsay.basicapplication.R

class ProductSellerCard(private val view: View) {

    private val productImage: ImageView = view.findViewById(R.id.productImage)
    private val productName: TextView = view.findViewById(R.id.productName)
    private val productPrice: TextView = view.findViewById(R.id.productPrice)
    private val productStock: TextView = view.findViewById(R.id.productStock)
    private val quantity: EditText = view.findViewById(R.id.quantity)
    private val increaseBtn: ImageButton = view.findViewById(R.id.increaseBtn)
    private val decreaseBtn: ImageButton = view.findViewById(R.id.decreaseBtn)
    private val sellBtn: Button = view.findViewById(R.id.sellBtn)

    private var currentQuantity: Int = 1
    private var maxStock: Int = 0

}