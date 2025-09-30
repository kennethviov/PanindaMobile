package dev.komsay.panindamobile

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProfilePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profilePage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //animated bg
        val rootLayout = findViewById<ConstraintLayout>(R.id.profilePage)
        rootLayout.setBackgroundResource(R.drawable.animation_bg)
        val animationDrawable = rootLayout.background as AnimationDrawable
        animationDrawable.start()


        // navbar
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

    override fun onResume() {
        super.onResume()

        val rootLayout = findViewById<ConstraintLayout>(R.id.profilePage)
        val animationDrawable = rootLayout.background as AnimationDrawable
        animationDrawable.start()
    }

    override fun onPause() {
        super.onPause()

        val rootLayout = findViewById<ConstraintLayout>(R.id.profilePage)
        val animationDrawable = rootLayout.background as AnimationDrawable
        animationDrawable.stop()
    }
}