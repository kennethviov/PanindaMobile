package dev.komsay.panindamobile

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import dev.komsay.panindamobile.ui.components.CategoryComponent
import dev.komsay.panindamobile.ui.components.ProductSellerComponent
import dev.komsay.panindamobile.ui.data.Product
import dev.komsay.panindamobile.ui.components.TopSellingProductComponent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.widget.LinearLayout
import dev.komsay.panindamobile.ui.components.NavigationBarManager

class HomePage : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.homePage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navigationBarManager = NavigationBarManager(this, findViewById(R.id.navbar))
        navigationBarManager.setup()

        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
        val formattedDateTime = currentDateTime.format(formatter)

        val receivedUsername = intent.getStringExtra("USERNAME_KEY")
        val userTxtView = findViewById<TextView>(R.id.userTextView)
        val dateTime = findViewById<TextView>(R.id.dateTimeView)

        userTxtView.text = "Hello, $receivedUsername"
        dateTime.text =formattedDateTime.toString()




        val container = findViewById<LinearLayout>(R.id.productSellerContainer)

        val app = application as Paninda
        val dataHelper = app.dataHelper

        val products = dataHelper.getAllProducts()
        val categories = dataHelper.getAllCategories()

        val topSellComp = TopSellingProductComponent(container)
        topSellComp.bind(products.find { it.unitSold == products.maxByOrNull { it.unitSold }?.unitSold }!!, totalUnitSold(products))

        for (cat in categories) {
            val catComp = CategoryComponent(findViewById(R.id.categorySlider))
            catComp.bind(cat)
        }

        for (product in products) {
            val component = ProductSellerComponent(container)
            component.bind(product) { selectedProduct, quantity ->
                handleSellClick(selectedProduct, quantity)
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun handleSellClick(product: Product, quantity: Int) {
        val totalAmount = product.price * quantity

        if(quantity <= 0) {
            Toast.makeText(
                this,
                "Quantity must be greater then 0",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        if (quantity <= product.stock) {
            Toast.makeText(
                this,
                "Not enough stock for ${product.name}",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        product.stock -= quantity  // to properly reduce stock

        Toast.makeText(
            this,
            "Selling ${quantity}x ${product.name} for ${String.format("₱%.2f", totalAmount)}. " +
                    "Remaining stock: ${product.stock}",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun totalUnitSold(products: List<Product>): Int {
        return products.sumOf { it.unitSold }
    }
}
