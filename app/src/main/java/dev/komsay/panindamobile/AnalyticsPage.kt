package dev.komsay.panindamobile

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

        val app = application as Paninda
        val dataHelper = app.dataHelper

        // get mock data
        val sales = dataHelper.getAllSales()

        // set up time filter buttons
        setUpTimeFilter()

        /* TODO
        *   - Data presentation
        *       - line graph for income
        *       - pie chart for individual products performance
        * */

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
            color = "#2196F3".toColorInt()
            valueTextColor = Color.BLACK
            lineWidth = 2f
            circleRadius = 5f
            setCircleColor("#2196F3".toColorInt())
        }

        val lineData = LineData(lineDataSet)

        lineChart.data = lineData
        lineChart.description.isEnabled = false

        // X-axis bottom
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        lineChart.axisRight.isEnabled = false  // disable right y-axis
        lineChart.animateX(1200)

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
            "#4CAF50".toColorInt(),  // Green
            "#F44336".toColorInt(),  // Red
            "#2196F3".toColorInt(),  // Blue
            "#FF9800".toColorInt()   // Orange
        )
        dataSet.valueTextSize = 14f

        val pieData = PieData(dataSet)

        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.centerText = "Monthly"
        pieChart.setCenterTextSize(18f)
        pieChart.animateY(1000)

        val navigationBarManager = NavigationBarManager(this, findViewById(R.id.navbar))
        navigationBarManager.setup()
    }

    // +---------------+
    // |  Time Filter  |
    // +---------------+
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
