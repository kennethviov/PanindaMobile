package dev.komsay.panindamobile.ui.components

import android.view.LayoutInflater
import android.view.View
import android.widget.*
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.ui.data.Product

class ProductSellerComponent {

    private val view: View
    private val productImage: ImageView
    private val productName: TextView
    private val productPrice: TextView
    private val productStock: TextView
    private val quantity: EditText
    private val increaseBtn: Button
    private val decreaseBtn: Button
    private val sellBtn: Button

    private var currentQuantity: Int = 1
    private var maxStock: Int = 0
    private var currentProduct: Product? = null

    constructor(container: LinearLayout) {
        view = LayoutInflater.from(container.context)
            .inflate(R.layout.component_product_seller, container, false)

        container.addView(view)

        productImage = view.findViewById(R.id.productImage)
        productName = view.findViewById(R.id.productName)
        productPrice = view.findViewById(R.id.productPrice)
        productStock = view.findViewById(R.id.productStock)
        quantity = view.findViewById(R.id.quantity)
        increaseBtn = view.findViewById(R.id.increaseBtn)
        decreaseBtn = view.findViewById(R.id.decreaseBtn)
        sellBtn = view.findViewById(R.id.sellBtn)
    }

    fun bind(product: Product, onSellClick: (Product, Int) -> Unit) {

        currentProduct = product
        refreshUI()

        setUpQuantityControls()

        sellBtn.setOnClickListener {
            if (product.isQuantityAvailable(currentQuantity)) {
                if (product.deductStock(currentQuantity)) {
                    onSellClick(product, currentQuantity)
                    refreshUI()
                } else {
                    Toast.makeText(view.context, "Failed to process sale", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(view.context, "Insufficient stock available", Toast.LENGTH_SHORT).show()
            }
        }

        updateUIState(product)

    }

    private fun refreshUI() {
        currentProduct?.let { product ->
            maxStock = product.stock

            productName.text = product.name
            productPrice.text = product.getFormattedPrice()
            productStock.text = product.stock.toString()

            product.imageResId?.let { resourceId ->
                productImage.setImageResource(resourceId)
            }

            // Reset quantity to 1 or adjust if current quantity exceeds new stock
            currentQuantity = if (currentQuantity > maxStock && maxStock > 0) maxStock else 1
            if (maxStock == 0) currentQuantity = 0

            quantity.setText(if (maxStock > 0) currentQuantity.toString() else "0")

            updateUIState(product)
        }
    }

    private fun setUpQuantityControls() {
        decreaseBtn.setOnClickListener {
            if (currentQuantity > 1) {
                currentQuantity--
                quantity.setText(currentQuantity.toString())
                updateButtonStates()
            }
        }

        increaseBtn.setOnClickListener {
            if (currentQuantity < maxStock) {
                currentQuantity++
                quantity.setText(currentQuantity.toString())
                updateButtonStates()
            }
        }

        quantity.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val inputQuantity = quantity.text.toString().toIntOrNull() ?: 1
                currentQuantity = when {
                    inputQuantity < 1 -> 1
                    inputQuantity > maxStock -> maxStock
                    else -> inputQuantity
                }
                quantity.setText(if (maxStock > 0) currentQuantity.toString() else "0")
                updateButtonStates()
            }
        }
    }

    private fun updateButtonStates() {
        decreaseBtn.isEnabled = currentQuantity > 1
        increaseBtn.isEnabled = currentQuantity < maxStock

        decreaseBtn.alpha = if (currentQuantity > 1) 1.0f else 0.5f
        increaseBtn.alpha = if (currentQuantity < maxStock) 1.0f else 0.5f
    }

    private fun updateUIState(product: Product) {
        val isInStock = product.isInStock()

        decreaseBtn.isEnabled = isInStock
        increaseBtn.isEnabled = isInStock
        quantity.isEnabled = isInStock
        sellBtn.isEnabled = isInStock

        if (isInStock) {
            sellBtn.text = "Sell"
            sellBtn.alpha = 1.0f
        } else {
            sellBtn.text = "Out of Stock"
            sellBtn.alpha = 0.5f
        }

        updateButtonStates()
    }
}