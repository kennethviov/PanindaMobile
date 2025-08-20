package dev.komsay.basicapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import dev.komsay.basicapplication.ui.components.ProductSellerComponent
import dev.komsay.basicapplication.ui.components.Product
import dev.komsay.basicapplication.ui.components.TopSellingProductComponent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomePage : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
        val formattedDateTime = currentDateTime.format(formatter)

        val receivedUsername = intent.getStringExtra("USERNAME_KEY")
        val userTxtView = findViewById<TextView>(R.id.userTextView)
        val dateTime = findViewById<TextView>(R.id.dateTimeView)

        userTxtView.text = "Hello, $receivedUsername"
        dateTime.text =formattedDateTime.toString()

        val products = mutableListOf<Product>(
            Product(
                id = "1",
                name = "Piattos",
                price = 10.50,
                stock = 20,
                imageResId = R.drawable.piattos
            ),
            Product(
                id = "2",
                name = "Banana",
                price = 15.00,
                stock = 15,
                imageResId = R.drawable.placeholder
            ),
            Product(
                id = "3",
                name = "Apple",
                price = 10.00,
                stock = 12,
                imageResId = R.drawable.placeholder
            ),
            Product(
                id = "4",
                name = "Mango",
                price = 25.00,
                stock = 8,
                imageResId = R.drawable.placeholder
            )
        )

        val comp = TopSellingProductComponent(findViewById(R.id.productSellerContainer))
        comp.bind(products[0])

        for (product in products) {
            val component = ProductSellerComponent(findViewById(R.id.productSellerContainer))
            component.bind(product) { selectedProduct, quantity ->
                handleSellClick(selectedProduct, quantity)
            }
        }
    }

    private fun handleSellClick(product: Product, quantity: Int) {
        val totalAmount = product.price * quantity
        product.stock - quantity

        Toast.makeText(
            this,
            "Selling ${quantity}x ${product.name} for ${String.format("₱%.2f", totalAmount)}",
            Toast.LENGTH_LONG
        ).show()
    }
}