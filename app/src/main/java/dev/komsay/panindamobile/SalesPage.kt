package dev.komsay.panindamobile

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.komsay.panindamobile.ui.components.NavigationBarManager
import dev.komsay.panindamobile.ui.components.ProductSalesComponent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SalesPage : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sales_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.salesPage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navigationBarManager = NavigationBarManager(this, findViewById(R.id.navbar))
        navigationBarManager.setup()

        val app = application as Paninda
        val dataHelper = app.dataHelper

        val sales = dataHelper.getAllSales()

        Log.i("SalesPage", "Sales: $sales")

        val container = findViewById<LinearLayout>(R.id.productSalesContainer)

        var currDate = LocalDateTime.parse(sales[0].salesDate)
        addDate(currDate, container)

        for (sale in sales) {

            val date = LocalDateTime.parse(sale.salesDate)

            if (date.dayOfMonth != currDate.dayOfMonth || date.monthValue != currDate.monthValue || date.year != currDate.year) {
                addDate(date, container)
                currDate = date
            }

            val component = ProductSalesComponent(container, this)
            component.bind(sale)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addDate(date: LocalDateTime, container: LinearLayout) {
        val textView = TextView(this)

        val txtDate = date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))

        textView.text = txtDate
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        textView.setTextColor(resources.getColor(R.color.black))
        textView.setTypeface(ResourcesCompat.getFont(this, R.font.inter_bold))
        textView.setPadding(8.toDp(this), 6.toDp(this), 8.toDp(this), 0.toDp(this))

        container.addView(textView)
    }

    private fun Int.toDp(context: Context): Int
    {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    /* TODO: Timely report
    *   - today
    *   - last 7 days
    *   - 30 days
    *   - all time
    * */
}