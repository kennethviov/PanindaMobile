package dev.komsay.panindamobile
import android.content.Intent

import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
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
import dev.komsay.panindamobile.ui.data.Category
import dev.komsay.panindamobile.ui.data.ProductSales
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.widget.LinearLayout

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

        val navHome = findViewById<ImageButton>(R.id.navHome)
        val navSales = findViewById<ImageButton>(R.id.navSales)
        val navProfile = findViewById<ImageButton>(R.id.navProfile)
        val navInventory = findViewById<ImageButton>(R.id.navBox)
        val navStats = findViewById<ImageButton>(R.id.navStats)

        navHome.setOnClickListener {
            if (this::class.java != HomePage::class.java) {
                val intent = Intent(this, HomePage::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        }

        navSales.setOnClickListener {
            if (this::class.java != SalesPage::class.java) {
                val intent = Intent(this, SalesPage::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        }

        navProfile.setOnClickListener {
            if (this::class.java != ProfilePage::class.java) {
                val intent = Intent(this, ProfilePage::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        }

        navInventory.setOnClickListener {
            if (this::class.java != InventoryPage::class.java) {
                val intent = Intent(this, InventoryPage::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        }

        navStats.setOnClickListener {
            if (this::class.java != StatisticsPage::class.java) {
                val intent = Intent(this, StatisticsPage::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        }


        userTxtView.text = "Hello, $receivedUsername"
        dateTime.text =formattedDateTime.toString()

        val products = mutableListOf<Product>(
            Product(
                id = "1",
                name = "Piattos",
                price = 10.50,
                stock = 20,
                category = "Snacks",
                imageResId = R.drawable.img_piattos
            ),
            Product(
                id = "2",
                name = "Banana",
                price = 15.00,
                stock = 15,
                category = "Snacks",
                imageResId = R.drawable.img_placeholder
            ),
            Product(
                id = "3",
                name = "Apple",
                price = 10.00,
                stock = 12,
                category = "Snacks",
                imageResId = R.drawable.img_placeholder
            ),
            Product(
                id = "4",
                name = "Mango",
                price = 25.00,
                stock = 8,
                category = "Snacks",
                imageResId = R.drawable.img_placeholder
            ),
            Product(
                id = "5",
                name = "Orange",
                price = 15.00,
                stock = 10,
                category = "Snacks",
                imageResId = R.drawable.img_placeholder
            ),
            Product(
                id = "6",
                name = "Coffee",
                price = 20.00,
                stock = 5,
                category = "Beverages",
                imageResId = R.drawable.img_placeholder
            ),
            Product(
                id = "7",
                name = "Tea",
                price = 15.00,
                stock = 10,
                category = "Beverages",
                imageResId = R.drawable.img_placeholder
            ),
            Product(
                id = "8",
                name = "Water",
                price = 10.00,
                stock = 20,
                category = "Beverages",
                imageResId = R.drawable.img_placeholder
            ),
            Product(
                id = "9",
                name = "Coke",
                price = 15.00,
                stock = 15,
                category = "Beverages",
                imageResId = R.drawable.img_placeholder
            ),
        )

        val categories = mutableListOf<Category>(

            Category(
                id = "1",
                name = "Snacks"
            ),
            Category(
                id = "2",
                name = "Beverages"
            ),
            Category(
                id = "3",
                name = "Fruits"
            ),
            Category(
                id = "4",
                name = "Vegetables"
            ),
            Category(
                id = "5",
                name = "Dairy"
            ),
            Category(
                id = "6",
                name = "Bakery"
            ),
            Category(
                id = "7",
                name = "Frozen Foods"
            ),
            Category(
                id = "8",
                name = "Meat"
            )

        )

        val product = ProductSales(
            id = "1",
            name = "Piattos",
            stockSold = 10,
            totalSales = 100.00,
            imageResId = R.drawable.img_piattos,
            overAllStocksSold = 67
        )

        val container = findViewById<LinearLayout>(R.id.productSellerContainer)

        val topSellComp = TopSellingProductComponent(container)
        topSellComp.bind(product)

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

    private fun handleSellClick(product: Product, quantity: Int) {
        val totalAmount = product.price * quantity

        if (quantity > product.stock) {
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

}