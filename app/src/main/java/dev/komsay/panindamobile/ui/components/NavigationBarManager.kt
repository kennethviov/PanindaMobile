package dev.komsay.panindamobile.ui.components

import android.content.Intent
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import dev.komsay.panindamobile.AnalyticsPage
import dev.komsay.panindamobile.HomePage
import dev.komsay.panindamobile.InventoryPage
import dev.komsay.panindamobile.ProfilePage
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.SalesPage

class NavigationBarManager(private val activity: AppCompatActivity, private val view: View) {

    fun setup() {
        val navHome = view.findViewById<ImageButton>(R.id.navHome)
        val navSales = view.findViewById<ImageButton>(R.id.navSales)
        val navProfile = view.findViewById<ImageButton>(R.id.navProfile)
        val navInventory = view.findViewById<ImageButton>(R.id.navBox)
        val navStats = view.findViewById<ImageButton>(R.id.navStats)

        navHome.setOnClickListener {
            navigateTo(HomePage::class.java)
        }

        navSales.setOnClickListener {
            navigateTo(SalesPage::class.java)
        }

        navProfile.setOnClickListener {
            navigateTo(ProfilePage::class.java)
        }

        navInventory.setOnClickListener {
            navigateTo(InventoryPage::class.java)
        }

        navStats.setOnClickListener {
            navigateTo(AnalyticsPage::class.java)
        }
    }

    private fun <T> navigateTo(targetActivity: Class<T>) {
        if (activity::class.java != targetActivity) {
            val intent = Intent(activity, targetActivity)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity.startActivity(intent)

            activity.overridePendingTransition(0, 0)
        }
    }
}