package dev.komsay.panindamobile.ui.pages

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
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
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dev.komsay.panindamobile.Paninda
import dev.komsay.panindamobile.R

class AnalyticsPage : AppCompatActivity() {

    private lateinit var todayButton : Button
    private lateinit var pastWeekButton : Button
    private lateinit var pastMonthButton : Button
    private lateinit var allTimeButton : Button

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

        // Initialize views
        val lineChart = findViewById<LineChart>(R.id.lineChart)
        val pieChart = findViewById<PieChart>(R.id.pieChart)
        val barChart = findViewById<BarChart>(R.id.barChart)

        // +-------------+
        // |  MOCK DATA  |
        // +-------------+
        val app = application as Paninda
        val dataHelper = app.dataHelper

        // get mock data
        val sales = dataHelper.getAllSales()

        // set up ui
        setUpTimeFilter()
        refreshLineChart(lineChart)
        refreshPieChart(pieChart)
        refreshBarChart(barChart)
    }

    private fun refreshLineChart(lineChart: LineChart) {

        val dates = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun"
        )

        val amounts = listOf(
            100f, 600f, 150f, 700f, 450f, 350f
        )

        val lineEntries = ArrayList<Entry>()
        amounts.forEachIndexed { index, value ->
            lineEntries.add(Entry(index.toFloat(), value))
        }

        val dataSet = LineDataSet(lineEntries, "Income")
        dataSet.color = "#C05454".toColorInt()              // line color
        dataSet.setDrawCircles(false)                       // remove points
        dataSet.lineWidth = 2f
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER        // smooth curves
        dataSet.setDrawFilled(true)

        val drawable = ContextCompat.getDrawable(this, R.drawable.bg_chart_gradient)
        dataSet.fillDrawable = drawable

        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.BLACK
        dataSet.setDrawValues(false)

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.valueFormatter = IndexAxisValueFormatter(dates)
        xAxis.textColor = "#C05454".toColorInt()

        lineChart.axisLeft.textColor = "#C05454".toColorInt()    // y axis text colors
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
    }

    private fun refreshPieChart(pieChart: PieChart) {
        val pieEntries = ArrayList<PieEntry>().apply {
            add(PieEntry(40f, "Food"))
            add(PieEntry(30f, "Bills"))
            add(PieEntry(20f, "Savings"))
            add(PieEntry(10f, "Other"))
        }

        val pieDataSet = PieDataSet(pieEntries, "Expenses")
        pieDataSet.colors = listOf(
            "#4CAF50".toColorInt(),  // Green
            "#F44336".toColorInt(),  // Red
            "#2196F3".toColorInt(),  // Blue
            "#FF9800".toColorInt()   // Orange
        )
        pieDataSet.valueTextSize = 14f

        val pieData = PieData(pieDataSet)

        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.centerText = "Monthly"
        pieChart.setCenterTextSize(18f)
        pieChart.animateY(1000)
    }

    private fun refreshBarChart(barChart: BarChart) {

        val labels = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun"
        )

        val inflowValues = listOf(
            120f, 150f, 90f, 200f, 170f, 210f
        )
        val outflowValues = listOf(
            80f, 110f, 60f, 140f, 130f, 160f
        )

        val inflow = ArrayList<BarEntry>()
        val outflow = ArrayList<BarEntry>()

        for (i in labels.indices) {
            inflow.add(BarEntry(i.toFloat(), inflowValues[i]))
            outflow.add(BarEntry(i.toFloat(), outflowValues[i]))
        }

        val inflowSet = BarDataSet(inflow, "Inflow").apply {
            color = Color.parseColor("#C05454")
        }

        val outflowSet = BarDataSet(outflow, "Outflow").apply {
            color = Color.parseColor("#FFC6AE")
        }

        val data = BarData(inflowSet, outflowSet)

        // 👉 These values WORK 100% for 2 datasets
        val barWidth = 0.3f
        val barSpace = 0.02f
        val groupSpace = 0.2f

        data.barWidth = barWidth

        barChart.data = data

        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.textColor = Color.DKGRAY

        barChart.axisRight.isEnabled = false
        barChart.axisLeft.setDrawGridLines(false)
        barChart.description.isEnabled = false

        // REQUIRED ORDER ⚠️
        barChart.xAxis.axisMinimum = 0f
        barChart.xAxis.axisMaximum = data.getGroupWidth(groupSpace, barSpace) * labels.size

        barChart.setScaleEnabled(false)
        barChart.setPinchZoom(false)
        barChart.isDoubleTapToZoomEnabled = false
        barChart.setHighlightPerDragEnabled(false)

        barChart.groupBars(0f, groupSpace, barSpace)

        barChart.invalidate()
    }

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
                    animateBackgroundTint(prev, selectedTint,defaultTint)
                }

                animateBackgroundTint(btn, defaultTint, selectedTint)

                selectedButton = btn

                // TODO: filtering logic here ->

            }
        }

        allTimeButton.post {
            animateBackgroundTint(allTimeButton, defaultTint, selectedTint)
            selectedButton = allTimeButton
        }
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
