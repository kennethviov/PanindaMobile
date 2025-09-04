package dev.komsay.panindamobile

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class ProfilePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)


        val navHome = findViewById<ImageButton>(R.id.navHome)
        val navSales = findViewById<ImageButton>(R.id.navSales)
        val navProfile = findViewById<ImageButton>(R.id.navProfile)
        val navInventory = findViewById<ImageButton>(R.id.navBox)
        val navStats = findViewById<ImageButton>(R.id.navStats)




        //dli na siya murag mo animate pag tuplok nimo profile profile ra nya way animation same sa tanan ni
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
    }
}

