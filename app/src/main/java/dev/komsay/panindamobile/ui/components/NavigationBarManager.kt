package dev.komsay.panindamobile.ui.components

import android.content.Intent
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import dev.komsay.panindamobile.ui.pages.AnalyticsPage
import dev.komsay.panindamobile.ui.pages.HomePage
import dev.komsay.panindamobile.ui.pages.InventoryPage
import dev.komsay.panindamobile.ui.pages.ProfilePage
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.ui.pages.SalesPage

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

    fun highlightActivePage(activeId: Int) {
        val indicators = listOf(
            view.findViewById<View>(R.id.indicatorHome),
            view.findViewById<View>(R.id.indicatorSales),
            view.findViewById<View>(R.id.indicatorProfile),
            view.findViewById<View>(R.id.indicatorInventory),
            view.findViewById<View>(R.id.indicatorAnalytics)
        )

        val buttons = listOf(
            view.findViewById<ImageButton>(R.id.navHome),
            view.findViewById<ImageButton>(R.id.navSales),
            view.findViewById<ImageButton>(R.id.navProfile),
            view.findViewById<ImageButton>(R.id.navBox),
            view.findViewById<ImageButton>(R.id.navStats)
        )

        indicators.forEach { it.visibility = View.GONE }

        setIconToDefault()
        setSelectedIcon(activeId, indicators)

        view.findViewById<View>(activeId).visibility = View.VISIBLE
    }

    private fun setIconToDefault() {
        view.findViewById<ImageButton>(R.id.navHome).setImageResource(R.drawable.ic_home_d)
        view.findViewById<ImageButton>(R.id.navSales).setImageResource(R.drawable.ic_sales_d)
        view.findViewById<ImageButton>(R.id.navProfile).setImageResource(R.drawable.ic_profile_d)
        view.findViewById<ImageButton>(R.id.navBox).setImageResource(R.drawable.ic_box_d)
        view.findViewById<ImageButton>(R.id.navStats).setImageResource(R.drawable.ic_statistics_d)
    }

    private fun setSelectedIcon(activeId: Int, indicators: List<View>) {
        val buttons = listOf(
            view.findViewById<ImageButton>(R.id.navHome),
            view.findViewById<ImageButton>(R.id.navSales),
            view.findViewById<ImageButton>(R.id.navProfile),
            view.findViewById<ImageButton>(R.id.navBox),
            view.findViewById<ImageButton>(R.id.navStats)
        )

        if (activeId == R.id.indicatorHome)
            buttons[0].setImageResource(R.drawable.ic_home_s)
        if (activeId == R.id.indicatorSales)
            buttons[1].setImageResource(R.drawable.ic_sales_s)
        if (activeId == R.id.indicatorProfile)
            buttons[2].setImageResource(R.drawable.ic_profile_s)
        if (activeId == R.id.indicatorInventory)
            buttons[3].setImageResource(R.drawable.ic_box_s)
        if (activeId == R.id.indicatorAnalytics)
            buttons[4].setImageResource(R.drawable.ic_statistics_s)
    }
}