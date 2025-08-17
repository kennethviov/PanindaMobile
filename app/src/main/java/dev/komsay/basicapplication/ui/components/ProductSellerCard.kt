package dev.komsay.basicapplication.ui.components

import android.view.View
import android.widget.*
import dev.komsay.basicapplication.R

class ProductSellerCard(private val container: LinearLayout) {

    private val view: View = View.inflate(container.context, R.layout.product_seller_component, container)

    private val productImage: ImageView = view.findViewById(R.id.productImage)
    private val productName: TextView = view.findViewById(R.id.productName)
    private val productPrice: TextView = view.findViewById(R.id.productPrice)
    private val productStock: TextView = view.findViewById(R.id.productStock)
    private val quantity: EditText = view.findViewById(R.id.quantity)
    private val increaseBtn: Button = view.findViewById(R.id.increaseBtn)
    private val decreaseBtn: Button = view.findViewById(R.id.decreaseBtn)
    private val sellBtn: Button = view.findViewById(R.id.sellBtn)

    private var currentQuantity: Int = 1
    private var maxStock: Int = 0

    fun bind(product: ProductSellerItem, onSellClick: (ProductSellerItem, Int) -> Unit) {
        maxStock = product.stock

        productName.text = product.name
        productPrice.text = product.getFormattedPrice()
        productStock.text = product.stock.toString()

        product.imageResId?.let { resourceId ->
            productImage.setImageResource(resourceId)
        }

        currentQuantity = 1
        quantity.setText(currentQuantity.toString())

        setUpQuantityControls()

        sellBtn.setOnClickListener {
            if (product.isQuantityAvailable(currentQuantity)) {
                onSellClick(product, currentQuantity)
            } else {
                Toast.makeText(view.context, "Insufficient stock available", Toast.LENGTH_SHORT).show()
            }
        }

        updateUIState(product)
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
                quantity.setText(currentQuantity.toString())
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

    private fun updateUIState(product: ProductSellerItem) {
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