package dev.komsay.panindamobile

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import dev.komsay.panindamobile.ui.components.ProductInventoryComponent
import dev.komsay.panindamobile.ui.components.ProductSellerComponent
import dev.komsay.panindamobile.ui.data.Product

class InventoryPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inventory_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.inventoryPage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val addBtn = findViewById<ImageButton>(R.id.addBtn)

        addBtn.setOnClickListener {
            val dialog = AddProduct(this)
            dialog.show()
        }

        // mock products
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

        val container = findViewById<LinearLayout>(R.id.productInventoryContainer)

        for (product in products) {
            val component = ProductInventoryComponent(container)
            component.bind(product)
        }

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
            if (this::class.java != AnalyticsPage::class.java) {
                val intent = Intent(this, AnalyticsPage::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        }
    }
}