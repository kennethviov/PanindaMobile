package dev.komsay.panindamobile.ui.components

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.time.format.DateTimeFormatter
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.ui.data.Sales
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(sales: Sales) {

        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val dateTime = LocalDateTime.parse(sales.salesDate)

        time.text = dateTime.format(formatter)
        salesID.text = sales.id
        productSales.text = sales.getFormattedTotal(sales.salesItems.sumOf { it.price * it.stock })

        view.setOnClickListener {
            val dialog = SalesDetails(context)
            dialog.show(sales)
        }
    }
}