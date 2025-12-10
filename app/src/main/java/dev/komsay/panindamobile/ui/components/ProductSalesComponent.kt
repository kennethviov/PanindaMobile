package dev.komsay.panindamobile.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.time.format.DateTimeFormatter
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.backend.dto.SalesDTO
import dev.komsay.panindamobile.ui.data.Sale
import dev.komsay.panindamobile.ui.fragments.SalesDetails
import java.time.LocalDateTime

class ProductSalesComponent {

    private val view: View
    private val context: Context
    private val time: TextView
    private val salesID: TextView
    private val productSales: TextView

    constructor(container: LinearLayout, context: Context) {
        this.context = context

        view = LayoutInflater.from(container.context)
            .inflate(R.layout.component_product_sales,
            container, false)

        container.addView(view)

        time = view.findViewById(R.id.time)
        salesID = view.findViewById(R.id.salesID)
        productSales = view.findViewById(R.id.productSales)

    }

    @SuppressLint("DefaultLocale")
    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(sales: SalesDTO) {

        val formatter = DateTimeFormatter.ofPattern("hh:mm a")
        val dateTime = LocalDateTime.parse(sales.salesDate)

        time.text = dateTime.format(formatter)
        salesID.text = sales.id.toString()
        productSales.text = String.format("₱%.2f",sales.totalPrice)

        view.setOnClickListener {
            val dialog = SalesDetails(context)
            dialog.show(sales)
        }
    }
}