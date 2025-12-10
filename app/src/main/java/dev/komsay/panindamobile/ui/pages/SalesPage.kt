package dev.komsay.panindamobile.ui.pages

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dev.komsay.panindamobile.Paninda
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.backend.dto.SalesDTO
import dev.komsay.panindamobile.backend.network.RetrofitClient
import dev.komsay.panindamobile.ui.components.NavigationBarManager
import dev.komsay.panindamobile.ui.components.ProductSalesComponent
import dev.komsay.panindamobile.ui.data.Sale
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SalesPage : AppCompatActivity() {

    private lateinit var container: LinearLayout
    private lateinit var todayButton : Button
    private lateinit var pastWeekButton : Button
    private lateinit var pastMonthButton : Button
    private lateinit var allTimeButton : Button

    private var sales: List<SalesDTO> = mutableListOf()

    private var selectedButton: Button? = null

    // animation duration
    private val animationDuration = 220L


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sales_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.salesPage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Navigation bar
        val navigationBarManager = NavigationBarManager(this, findViewById(R.id.navbar)).apply {
            setup()
            highlightActivePage(R.id.indicatorSales)
        }

        // Initialize views
        container = findViewById(R.id.productSalesContainer)

        loadSales()

        // set up ui
        setUpTimeFilter()
        refreshSalesUI(this.sales)
    }



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
                    animateBackgroundTint(prev, selectedTint,defaultTint)
                }

                animateBackgroundTint(btn, defaultTint, selectedTint)

                selectedButton = btn

                val now = LocalDateTime.now()

                val (startDate, endDate) =
                    when (btn.text) {
                        "Today" -> {
                            LocalDate.now().atStartOfDay() to now
                        }
                        "Past Week" -> {
                            now.minusDays(7) to now
                        }
                        "Past Month" -> {
                            now.minusMonths(1) to now
                        }
                        else -> {
                            LocalDateTime.MIN to now
                        }
                    }

                val filteredSales = sales
                    .filter { LocalDateTime.parse(it.salesDate) in startDate..endDate }
                    .sortedByDescending { LocalDateTime.parse(it.salesDate) }

                refreshSalesUI(filteredSales)

            }
        }

        allTimeButton.post {
            animateBackgroundTint(allTimeButton, defaultTint, selectedTint)
            selectedButton = allTimeButton
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        loadSales()
        refreshSalesUI(sales)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadSales() {
        val salesApi = RetrofitClient.getSalesApi(this)

        lifecycleScope.launch {
            try {
                val fetchedSales = salesApi.getAllSales()
                sales = fetchedSales.reversed()

                Log.d("SalesPage: loadSales()", "Sales fetched: $fetchedSales")

                refreshSalesUI(sales)
            } catch (e: Exception) {
                e.printStackTrace()

                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Failed to load sales: ${e.message}",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshSalesUI(sales: List<SalesDTO>) {
        container.removeAllViews()

        var currDate = LocalDateTime.now()

        for (sale in sales) {

            val date = LocalDateTime.parse(sale.salesDate)

            if (date.dayOfMonth != currDate.dayOfMonth || date.monthValue != currDate.monthValue || date.year != currDate.year) {
                addDate(date, container)
                currDate = date
            }

            val component = ProductSalesComponent(container, this)
            component.bind(sale)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addDate(date: LocalDateTime, container: LinearLayout) {
        val textView = TextView(this)

        val txtDate = date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))

        textView.text = txtDate
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        textView.setTextColor(resources.getColor(R.color.black))
        textView.setTypeface(ResourcesCompat.getFont(this, R.font.inter_bold))
        textView.setPadding(8.toDp(this), 6.toDp(this), 8.toDp(this), 0.toDp(this))

        container.addView(textView)
    }

    private fun Int.toDp(context: Context): Int
    {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            context.resources.displayMetrics
        ).toInt()
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