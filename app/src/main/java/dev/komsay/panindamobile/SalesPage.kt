package dev.komsay.panindamobile

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.komsay.panindamobile.ui.components.NavigationBarManager
import dev.komsay.panindamobile.ui.components.ProductInventoryComponent
import dev.komsay.panindamobile.ui.components.ProductSalesComponent
import dev.komsay.panindamobile.ui.data.Product

class SalesPage : AppCompatActivity() {
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

        val products = dataHelper.getAllProducts()

        val container = findViewById<LinearLayout>(R.id.productSalesContainer)

        for (product in products) {
            val component = ProductSalesComponent(container)
            component.bind(product)
        }
    }


}