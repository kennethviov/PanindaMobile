package dev.komsay.panindamobile.ui.components

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import dev.komsay.panindamobile.R
import android.content.Context
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import dev.komsay.panindamobile.backend.dto.ProductsDTO
import dev.komsay.panindamobile.backend.dto.SalesItemsDTO
import dev.komsay.panindamobile.backend.service.SharedPrefManager
import dev.komsay.panindamobile.ui.data.CartItem
import dev.komsay.panindamobile.ui.pages.AddOrModifyProductPage

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
    fun bind(product: ProductsDTO,
             launcher: ActivityResultLauncher<Intent>,
             onProductUpdated: () -> Unit
    ) {

        view.setOnLongClickListener {
            val intent = Intent(context, AddOrModifyProductPage::class.java)
            intent.putExtra("PRODUCT_ID", product.id)
            launcher.launch(intent)
            true
        }

        productName.text = product.name
        productPrice.text = product.getFormattedPrice()
        productStock.text = product.stocks.toString()

        if (product.stocks <= 5) {
            val typeface = ResourcesCompat.getFont(context, R.font.inter_bold)
            productStock.setTypeface(typeface, android.graphics.Typeface.BOLD)
            productStock.setTextColor(context.getColor(R.color.red))
        }

        category = product.categoryName.toString()

        val token = SharedPrefManager.getToken(view.context)
        val imageUrl = "http://10.0.2.2:8080${product.imageUrl}"
        val glideUrl = GlideUrl(
            imageUrl,
            LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        )

        Glide.with(view.context)
            .load(glideUrl)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_placeholder)
            .into(productImage)

        container.addView(view)
    }

    @SuppressLint("DefaultLocale")
    fun bind(item: SalesItemsDTO) {

        productTotal.visibility = View.VISIBLE
        productTotal.text = String.format("₱%.2f", item.subtotal)

        productName.text = item.productName
        productPrice.text = String.format("₱%.2f", item.unitPrice)
        productStock.text = item.quantity.toString()

        container.addView(view)
    }

    fun getView(): View { return view }

}
