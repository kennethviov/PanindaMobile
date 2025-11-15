package dev.komsay.panindamobile

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.components.XAxis
import dev.komsay.panindamobile.ui.components.NavigationBarManager

class AnalyticsPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_analytics_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.analyticsPage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navigationBarManager = NavigationBarManager(this, findViewById(R.id.navbar))
        navigationBarManager.setup()

        // -------------------------
        // PIE CHART SETUP
        // -------------------------

        val pieChart = findViewById<PieChart>(R.id.pieChart)

        val entries = ArrayList<PieEntry>().apply {
            add(PieEntry(40f, "Food"))
            add(PieEntry(30f, "Bills"))
            add(PieEntry(20f, "Savings"))
            add(PieEntry(10f, "Other"))
        }

        val dataSet = PieDataSet(entries, "Expenses")
        dataSet.colors = listOf(
            Color.parseColor("#4CAF50"),  // Green
            Color.parseColor("#F44336"),  // Red
            Color.parseColor("#2196F3"),  // Blue
            Color.parseColor("#FF9800")   // Orange
        )
        dataSet.valueTextSize = 14f

        val pieData = PieData(dataSet)

        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.centerText = "Monthly"
        pieChart.setCenterTextSize(18f)
        pieChart.animateY(1000)


        // -------------------------
        // LINE CHART SETUP
        // -------------------------

        val lineChart = findViewById<LineChart>(R.id.lineChart)

        val lineEntries = ArrayList<Entry>().apply {
            add(Entry(0f, 5f))
            add(Entry(1f, 8f))
            add(Entry(2f, 3f))
            add(Entry(3f, 10f))
            add(Entry(4f, 6f))
        }

        val lineDataSet = LineDataSet(lineEntries, "Sales Trends").apply {
            color = Color.parseColor("#2196F3")
            valueTextColor = Color.BLACK
            lineWidth = 2f
            circleRadius = 5f
            setCircleColor(Color.parseColor("#2196F3"))
        }

        val lineData = LineData(lineDataSet)

        lineChart.data = lineData
        lineChart.description.isEnabled = false

        // X-axis bottom
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        lineChart.axisRight.isEnabled = false  // disable right y-axis
        lineChart.animateX(1200)
    }

    /* TODO
    *   - implement the analytics page functionalities
    *   - Data presentation
    *       - line graph for income
    *       - pie chart for individual products performance
    *   - Data retrieving and logic for calculation
    * */

}
