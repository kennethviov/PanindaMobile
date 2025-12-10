package dev.komsay.panindamobile.ui.pages

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.components.XAxis
import dev.komsay.panindamobile.ui.components.NavigationBarManager
import androidx.core.graphics.toColorInt
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.snackbar.Snackbar
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.backend.dto.SalesDTO
import dev.komsay.panindamobile.backend.dto.SalesItemsDTO
import dev.komsay.panindamobile.backend.network.RetrofitClient
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AnalyticsPage : AppCompatActivity() {

    private lateinit var todayButton : Button
    private lateinit var pastWeekButton : Button
    private lateinit var pastMonthButton : Button
    private lateinit var allTimeButton : Button

    private var sales: List<SalesDTO> = mutableListOf()
    private var salesItems: List<SalesItemsDTO> = mutableListOf()
    private var topProducts: List<Pair<String, Int>> = mutableListOf()


    private var selectedButton: Button? = null

    // animation duration
    private val animationDuration = 220L


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_analytics_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.analyticsPage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Navigation bar
        val navigationBarManager = NavigationBarManager(this, findViewById(R.id.navbar)).apply {
            setup()
            highlightActivePage(R.id.indicatorAnalytics)
        }

        // Set up UI components
        setUpTimeFilter()

        // Load data (charts will be refreshed when data loads)
        loadSales()
        loadSalesItems()

        // Initialize charts with empty state
        val lineChart = findViewById<LineChart>(R.id.lineChart)
        val pieChart = findViewById<PieChart>(R.id.pieChart)
        val barChart = findViewById<BarChart>(R.id.barChart)

        // Optional: Show loading state or placeholder
        lineChart.visibility = View.GONE
        pieChart.visibility = View.GONE
        // barChart stays as is with mock data for now
//        refreshBarChart(barChart)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun refreshLineChartWithData(lineChart: LineChart, salesData: List<SalesDTO>) {
        if (salesData.isEmpty()) {
            lineChart.visibility = View.GONE
            Log.d("AnalyticsPage", "No sales data to display")
            return
        }

        Log.d("AnalyticsPage", "Refreshing line chart with ${salesData.size} sales")

        lineChart.visibility = View.VISIBLE

        // Group sales by date and sum total amounts
        val salesByDate = salesData
            .groupBy { sale ->
                val dateTime = LocalDateTime.parse(sale.salesDate)
                dateTime.toLocalDate() // Keep as LocalDate for proper sorting
            }
            .map { (date, salesList) ->
                date to salesList.sumOf { it.totalPrice }.toFloat()
            }
            .sortedBy { it.first } // Sort by LocalDate (proper date sorting)
            .takeLast(10) // Get last 10 days

        if (salesByDate.isEmpty()) {
            lineChart.visibility = View.GONE
            Log.d("AnalyticsPage", "No grouped sales data")
            return
        }

        Log.d("AnalyticsPage", "Sales by date: $salesByDate")

        // Format dates for display
        val dateFormatter = DateTimeFormatter.ofPattern("MMM dd")
        val dates = salesByDate.map { it.first.format(dateFormatter) }
        val amounts = salesByDate.map { it.second }

        Log.d("AnalyticsPage", "Dates: $dates")
        Log.d("AnalyticsPage", "Amounts: $amounts")

        val lineEntries = ArrayList<Entry>()
        amounts.forEachIndexed { index, value ->
            lineEntries.add(Entry(index.toFloat(), value))
        }

        val dataSet = LineDataSet(lineEntries, "Revenue")
        dataSet.color = "#C05454".toColorInt()
        dataSet.setDrawCircles(true) // Enable circles to see data points
        dataSet.circleRadius = 4f
        dataSet.setCircleColor("#C05454".toColorInt())
        dataSet.lineWidth = 2f
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.setDrawFilled(true)

        val drawable = ContextCompat.getDrawable(this, R.drawable.bg_chart_gradient)
        dataSet.fillDrawable = drawable

        dataSet.valueTextSize = 10f
        dataSet.valueTextColor = Color.BLACK
        dataSet.setDrawValues(true) // Show values to debug

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.valueFormatter = IndexAxisValueFormatter(dates)
        xAxis.textColor = "#C05454".toColorInt()
        xAxis.labelRotationAngle = -45f
        xAxis.setLabelCount(dates.size, false)

        lineChart.axisLeft.textColor = "#C05454".toColorInt()
        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.xAxis.setDrawGridLines(false)

        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false

        lineChart.setScaleEnabled(false)
        lineChart.setPinchZoom(false)
        lineChart.isDoubleTapToZoomEnabled = false
        lineChart.setHighlightPerDragEnabled(false)

        lineChart.animateY(1000)
        lineChart.invalidate()

        Log.d("AnalyticsPage", "Line chart updated successfully")
    }

    private fun refreshPieChart(pieChart: PieChart) {
        if (topProducts.isEmpty()) {
            pieChart.visibility = View.GONE // Or show a "no data" message
            return
        }
        pieChart.visibility = View.VISIBLE

        val pieEntries = ArrayList<PieEntry>()
        topProducts.forEach { product ->
            pieEntries.add(PieEntry(product.second.toFloat(), product.first))
        }

        val pieDataSet = PieDataSet(pieEntries, "Top Products")
        pieDataSet.colors = listOf(
            "#4CAF50".toColorInt(),  // Green
            "#F44336".toColorInt(),  // Red
            "#2196F3".toColorInt(),  // Blue
            "#FF9800".toColorInt(),   // Orange
            "#9C27B0".toColorInt() // Purple for the 5th
        )
        pieDataSet.valueTextSize = 14f

        val pieData = PieData(pieDataSet)
        pieData.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        })

        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.centerText = "Top Products"
        pieChart.setCenterTextSize(18f)
        pieChart.animateY(1000)
    }

//    private fun refreshBarChart(barChart: BarChart) {
//
//        val labels = listOf(
//            "Jan", "Feb", "Mar", "Apr", "May", "Jun"
//        )
//
//        val inflowValues = listOf(
//            120f, 150f, 90f, 200f, 170f, 210f
//        )
//        val outflowValues = listOf(
//            80f, 110f, 60f, 140f, 130f, 160f
//        )
//
//        val inflow = ArrayList<BarEntry>()
//        val outflow = ArrayList<BarEntry>()
//
//        for (i in labels.indices) {
//            inflow.add(BarEntry(i.toFloat(), inflowValues[i]))
//            outflow.add(BarEntry(i.toFloat(), outflowValues[i]))
//        }
//
//        val inflowSet = BarDataSet(inflow, "Inflow").apply {
//            color = Color.parseColor("#C05454")
//        }
//
//        val outflowSet = BarDataSet(outflow, "Outflow").apply {
//            color = Color.parseColor("#FFC6AE")
//        }
//
//        val data = BarData(inflowSet, outflowSet)
//
//        // 👉 These values WORK 100% for 2 datasets
//        val barWidth = 0.3f
//        val barSpace = 0.02f
//        val groupSpace = 0.2f
//
//        data.barWidth = barWidth
//
//        barChart.data = data
//
//        val xAxis = barChart.xAxis
//        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
//        xAxis.position = XAxis.XAxisPosition.BOTTOM
//        xAxis.setDrawGridLines(false)
//        xAxis.granularity = 1f
//        xAxis.textColor = Color.DKGRAY
//
//        barChart.axisRight.isEnabled = false
//        barChart.axisLeft.setDrawGridLines(false)
//        barChart.description.isEnabled = false
//
//        barChart.xAxis.axisMinimum = 0f
//        barChart.xAxis.axisMaximum = data.getGroupWidth(groupSpace, barSpace) * labels.size
//
//        barChart.setScaleEnabled(false)
//        barChart.setPinchZoom(false)
//        barChart.isDoubleTapToZoomEnabled = false
//        barChart.setHighlightPerDragEnabled(false)
//
//        barChart.groupBars(0f, groupSpace, barSpace)
//
//        barChart.invalidate()
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpTimeFilter() {
        todayButton = findViewById(R.id.todayButton)
        pastWeekButton = findViewById(R.id.pastWeekButton)
        pastMonthButton = findViewById(R.id.pastMonthButton)
        allTimeButton = findViewById(R.id.allTimeButton)

        val buttons = listOf(todayButton, pastWeekButton, pastMonthButton, allTimeButton)

        val defaultTint = buttons.first().backgroundTintList?.defaultColor ?: Color.TRANSPARENT
        val selectedTint = ContextCompat.getColor(this, R.color.selectedTimeFilterColor)

        buttons.forEach { btn ->
            btn.setOnClickListener {
                if (selectedButton == btn) return@setOnClickListener

                selectedButton?.let { prev ->
                    animateBackgroundTint(prev, selectedTint, defaultTint)
                }

                animateBackgroundTint(btn, defaultTint, selectedTint)
                selectedButton = btn

                // Apply filtering logic
                filterSalesData(btn)
            }
        }

        allTimeButton.post {
            animateBackgroundTint(allTimeButton, defaultTint, selectedTint)
            selectedButton = allTimeButton
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filterSalesData(button: Button) {
        val now = LocalDateTime.now()

        val filteredSales = when (button.id) {
            R.id.todayButton -> {
                sales.filter {
                    val saleDate = LocalDateTime.parse(it.salesDate)
                    saleDate.toLocalDate() == now.toLocalDate()
                }
            }
            R.id.pastWeekButton -> {
                val weekAgo = now.minusWeeks(1)
                sales.filter {
                    val saleDate = LocalDateTime.parse(it.salesDate)
                    saleDate.isAfter(weekAgo)
                }
            }
            R.id.pastMonthButton -> {
                val monthAgo = now.minusMonths(1)
                sales.filter {
                    val saleDate = LocalDateTime.parse(it.salesDate)
                    saleDate.isAfter(monthAgo)
                }
            }
            else -> sales // All time
        }

        // Update line chart
        val lineChart = findViewById<LineChart>(R.id.lineChart)
        refreshLineChartWithData(lineChart, filteredSales)

        // --- NEW: update pie chart using salesItems that belong to filteredSales ---
        val pieChart = findViewById<PieChart>(R.id.pieChart)

        // If salesItems not loaded yet, use empty list (will hide pie)
        if (salesItems.isEmpty()) {
            topProducts = emptyList()
            refreshPieChart(pieChart)
            return
        }

        // Collect the IDs of filtered sales (safe: filter out null ids)
        val filteredSaleIds = filteredSales.mapNotNull { it.id }.toSet()

        // If "All time" was selected or filteredSaleIds empty (but button is all-time), use all salesItems
        val filteredSalesItemsForPie = if (button.id == R.id.allTimeButton) {
            salesItems
        } else {
            // Keep only items that belong to the filtered sales
            salesItems.filter { si -> si.salesId != null && filteredSaleIds.contains(si.salesId) }
        }

        // Recompute topProducts and refresh pie chart
        topProducts = getTopSellingProducts(filteredSalesItemsForPie)
        refreshPieChart(pieChart)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadSales() {
        val salesApi = RetrofitClient.getSalesApi(this)

        lifecycleScope.launch {
            try {
                val fetchedSales = salesApi.getAllSales()
                sales = fetchedSales // Don't reverse yet, we'll sort by date properly

                Log.d("AnalyticsPage", "Sales fetched: ${sales.size} items")

                // Log first few sales for debugging
                sales.take(3).forEach { sale ->
                    Log.d("AnalyticsPage", "Sale: date=${sale.salesDate}, total=${sale.totalPrice}")
                }

                // Refresh chart on UI thread after data is loaded
                runOnUiThread {
                    val lineChart = findViewById<LineChart>(R.id.lineChart)
                    refreshLineChartWithData(lineChart, sales)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("AnalyticsPage", "Error loading sales: ${e.message}", e)

                runOnUiThread {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Failed to load sales: ${e.message}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun loadSalesItems() {

        val api = RetrofitClient.getSalesItemsApi(this)

        lifecycleScope.launch {
            try {
                val fetchedSalesItems = api.getAllSalesItem()
                salesItems = fetchedSalesItems.reversed()

                Log.d("SalesPage: loadSalesItems()", "Sales items fetched: $fetchedSalesItems")

                topProducts = getTopSellingProducts(salesItems)
                Log.d("SalesPage: loadSalesItems()", "Top products: $topProducts")

                // Refresh chart on UI thread
                runOnUiThread {
                    val pieChart = findViewById<PieChart>(R.id.pieChart)
                    refreshPieChart(pieChart)
                }


            } catch (e: Exception) {
                e.printStackTrace()

                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Failed to load sales items: ${e.message}",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

    }

    private fun getTopSellingProducts(salesItems: List<SalesItemsDTO>): List<Pair<String, Int>> {
        return salesItems
            .groupBy { it.productName }
            .map { (name, items) ->
                name to items.sumOf { it.quantity }
            }
            .sortedByDescending { it.second }
            .take(5)
    }

    private fun animateBackgroundTint(button: Button, fromColor: Int, toColor: Int) {
        (button.getTag(R.id.animator_tag) as? ValueAnimator)?.cancel()

        val animator = ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor)
        animator.duration = animationDuration
        animator.addUpdateListener { valueAnimator ->
            val color = valueAnimator.animatedValue as Int
            button.backgroundTintList = ColorStateList.valueOf(color)
        }
        animator.start()

        button.setTag(R.id.animator_tag, animator)
    }

}
