package dev.komsay.panindamobile.ui.components

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.ui.data.Product
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import dev.komsay.panindamobile.Paninda
import dev.komsay.panindamobile.ui.data.CartItem
import dev.komsay.panindamobile.ui.fragments.AddProductDialogFragment

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(product: Product, onProductUpdated: () -> Unit) {

        view.setOnLongClickListener {
            val activity = context as AppCompatActivity
            val dialog = AddProductDialogFragment()
            dialog.populateForEdit(product)
            dialog.setOnProductAddedListener { name, price, stock, category ->
                val app = context.applicationContext as Paninda
                val dataHelper = app.dataHelper
                val updatedProduct = product.copy(
                    name = name,
                    price = price.toDouble(),
                    stock = stock.toInt(),
                    category = category
                )
                dataHelper.updateProduct(updatedProduct)
                onProductUpdated()
            }
            dialog.show(activity.supportFragmentManager, "AddProductDialogFragment")
            true
        }


        productName.text = product.name
        productPrice.text = product.getFormattedPrice(product.price)
        productStock.text = product.stock.toString()
        if (product.stock <= 5) {
            val typeface = ResourcesCompat.getFont(context, R.font.inter_bold)
            productStock.setTypeface(typeface, android.graphics.Typeface.BOLD)
            productStock.setTextColor(context.getColor(R.color.red))
        }

        category = product.category

        product.imageResId?.let { resourceId ->
            productImage.setImageResource(resourceId)
        }

        container.addView(view)
    }

    @SuppressLint("DefaultLocale")
    fun bind(item: CartItem) {

        productTotal.visibility = View.VISIBLE
        productTotal.text = String.format("₱%.2f", item.productPrice * item.productQuantity)

        productName.text = item.productName
        productPrice.text = String.format("₱%.2f", item.productPrice)
        productStock.text = item.productQuantity.toString()

        item.imageResId?.let { resourceId ->
            productImage.setImageResource(resourceId)
        }

        container.addView(view)
    }

    fun getView(): View { return view }

}
