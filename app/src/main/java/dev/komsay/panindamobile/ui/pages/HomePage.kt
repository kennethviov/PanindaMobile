package dev.komsay.panindamobile.ui.pages

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import androidx.annotation.RequiresApi
import dev.komsay.panindamobile.ui.components.ProductSellerComponent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import dev.komsay.panindamobile.ui.components.CartItemComponent
import dev.komsay.panindamobile.ui.components.NavigationBarManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dev.komsay.panindamobile.Paninda
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.backend.dto.CategoryDTO
import dev.komsay.panindamobile.backend.dto.ProductsDTO
import dev.komsay.panindamobile.backend.dto.SalesDTO
import dev.komsay.panindamobile.backend.dto.SalesItemsDTO
import dev.komsay.panindamobile.backend.network.RetrofitClient
import dev.komsay.panindamobile.backend.service.SharedPrefManager
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class HomePage : AppCompatActivity() {

    private lateinit var app: Paninda
    private lateinit var container: LinearLayout
    private lateinit var categorySlider: LinearLayout
    private lateinit var userTxtView: TextView
    private lateinit var dateTime: TextView
    private var products: List<ProductsDTO> = mutableListOf()
    private val cart: MutableList<SalesItemsDTO> = mutableListOf()
    private var categories: List<CategoryDTO> = mutableListOf()


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.homePage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Navigation bar
        val navigationBarManager = NavigationBarManager(this, findViewById(R.id.navbar)).apply {
            setup()
            highlightActivePage(R.id.indicatorHome)
        }

        // Initialize views
        container = findViewById(R.id.productSellerContainer)
        categorySlider = findViewById(R.id.categorySlider)
        userTxtView = findViewById(R.id.userTextView)
        dateTime = findViewById(R.id.dateTimeView)

        val receivedUsername = SharedPrefManager.getUsername(this)
        val cartCancel = findViewById<Button>(R.id.btn_cancel)
        val cartSell = findViewById<Button>(R.id.btn_sell)

        // Home Page  Greeting
        setUpGreetings(receivedUsername)

        // load data
        loadProductsFromDB()
        loadCategoriesFromDB()

        // refresh UI
        //refreshTopSellingProducts(this.products)
        refreshCategoryUI(this.categories)
        refreshProductUI(this.products)
        setUpCartBtn(cartCancel, cartSell)
        refreshCartUI()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        loadProductsFromDB()
        loadCategoriesFromDB()
    }

    @SuppressLint("DefaultLocale")
    private fun handleSellClick(product: ProductsDTO, quantity: Int) {

        if(quantity <= 0) {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Quantity must be greater then 0",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        if (quantity > product.stocks) {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Not enough stock for ${product.name}",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        if (cart.any { it.productName == product.name }) {
            val cartItem = cart.find { it.productName == product.name }!!

            if (cartItem.quantity + quantity > product.stocks) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Cannot add more than stock",
                    Snackbar.LENGTH_SHORT
                ).show()
                return
            }

            cartItem.quantity += quantity
            refreshCartUI()
            return
        }

        val cartItem = SalesItemsDTO(
            productId = product.id,
            productName = product.name,
            quantity = quantity,
            unitPrice = product.price,
            subtotal = (product.price * quantity)
        )
        cart.add(cartItem)
        toggleCartVisibility("visible")
        refreshCartUI()
    }

    private var currCat: Button? = null
    private var lastCat: Button? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun handleCategoryClick(category: Button) {
        if (currCat == category) {
            refreshProductUI(this.products)
            category.backgroundTintList = ContextCompat.getColorStateList(this@HomePage, R.color.categoryColor)
            currCat = null
            return
        }

        lastCat?.backgroundTintList = ContextCompat.getColorStateList(this@HomePage, R.color.categoryColor)

        val categorizedProducts = products.filter { it.categoryName == category.text }
        refreshProductUI(categorizedProducts)
        category.backgroundTintList = ContextCompat.getColorStateList(this@HomePage, R.color.selectedCategoryColor)
        currCat = category
        lastCat = currCat
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshCategoryUI(categories: List<CategoryDTO>) {
        categorySlider.removeAllViews()

        for (cat in categories) {
            val paddingDp = 12
            val scale = resources.displayMetrics.density
            val paddingPx = (paddingDp * scale + 0.5f).toInt()

            val button = Button(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                ).apply {
                    marginEnd = (8 * scale * 0.5f).toInt()
                }

                text = cat.categoryName
                textSize = 12f
                typeface = ResourcesCompat.getFont(this@HomePage, R.font.inter_regular)
                setTextColor(ContextCompat.getColor(this@HomePage, R.color.black))
                isAllCaps = false

                background = ContextCompat.getDrawable(this@HomePage, R.drawable.bg_category)
                backgroundTintList = ContextCompat.getColorStateList(this@HomePage, R.color.categoryColor)

                elevation = 0f
                translationZ = 0f
                stateListAnimator = null

                setPadding(paddingPx, 0, paddingPx, 0)
            }

            categorySlider.addView(button)
            button.setOnClickListener {
                handleCategoryClick(button)
            }
        }
    }

    fun refreshProductUI(products: List<ProductsDTO>) {
        container.removeAllViews()

        for (product in products) {
            val component = ProductSellerComponent(container)
            component.bind(product) { selectedProduct, quantity ->
                handleSellClick(selectedProduct, quantity)
            }
        }
    }

//    fun refreshTopSellingProducts(products: List<SalesDTO>) {
//        val topSellCont = findViewById<LinearLayout>(R.id.view_top_seller)
//        topSellCont.removeAllViews()
//        val topSellComp = TopSellingProductComponent(topSellCont)
//        topSellComp.bind(products.find { it -> it.unitSold == products.maxByOrNull { it.unitSold }?.unitSold }!!, totalUnitSold(products))
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpCartBtn(cancelBtn: Button, sellBtn: Button) {
        cancelBtn.setOnClickListener {
            cart.clear()
            toggleCartVisibility("gone")
        }

        sellBtn.setOnClickListener {

            val salesApi = RetrofitClient.getSalesApi(this)

            lifecycleScope.launch {
                try {
                    salesApi.sellMultiple(createSalesDTO())

                    setResult(RESULT_OK)
                } catch (e: Exception) {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Error: ${e.message}",
                        Snackbar.LENGTH_LONG
                    ).show()

                    Log.e("Add Sales", "Error", e)
                }
            }

            cart.clear()
            onResume()
            loadProductsFromDB()
            refreshProductUI(this.products)
            toggleCartVisibility("gone")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createSalesDTO(): SalesDTO {
        return SalesDTO(
            salesDate = LocalDateTime.now().toString(),
            totalPrice = cart.sumOf { it.subtotal },
            items = cart
        )
    }

    private fun refreshCartUI() {
        val sales = cart.sumOf { it.subtotal }
        val salesTotal = findViewById<TextView>(R.id.txt_total)
        val cartCont = findViewById<LinearLayout>(R.id.view_cart_items)

        salesTotal.text = String.format("₱%.2f", sales)
        cartCont.removeAllViews()
        for (item in cart) {
            val cartItem = CartItemComponent(cartCont)
            cartItem.bind(item)
        }
    }

    fun toggleCartVisibility(visibility: String?) {
        val cartLayout = findViewById<ConstraintLayout>(R.id.view_cart)

        val cartHeight = cartLayout.layoutParams.height
        val dfltBotPad = (100f / resources.displayMetrics.density).toInt()

        if (visibility != "visible") {
            cartLayout.visibility = View.GONE
            container.setPadding(0, 0, 0, dfltBotPad)
        } else {
            cartLayout.visibility = View.VISIBLE
            container.setPadding(0, 0, 0, cartHeight + dfltBotPad)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadProductsFromDB() {
        val productsApi = RetrofitClient.getProductsApi(this)

        lifecycleScope.launch {
            try {
                val fetchedProducts = productsApi.getAllProducts()
                Log.d("Home: loadProductsFromDB()", "Products fetched: $fetchedProducts")

                products = fetchedProducts

                refreshProductUI(products)

            } catch (e: Exception) {
                e.printStackTrace()

                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Failed to load products: ${e.message}",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadCategoriesFromDB() {

        val catApi = RetrofitClient.getCategoryApi(this)

        lifecycleScope.launch {
            try {
                val fetchedCategories = catApi.getAllCategories()
                Log.d("Home: loadCategoriesFromDB()", "Categories fetched: $fetchedCategories")

                categories = fetchedCategories

                refreshCategoryUI(categories)

            } catch (e: Exception) {
                e.printStackTrace()

                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Failed to load categories: ${e.message}",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    // private fun totalUnitSold(products: List<Product>): Int { return products.sumOf { it.unitSold } }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpGreetings(receivedUsername: String?) {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
        val formattedDateTime = currentDateTime.format(formatter)

        userTxtView.text = "Hello, ${receivedUsername ?: "Guest"}"
        dateTime.text =formattedDateTime.toString()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            currentFocus!!.clearFocus()
        }
        return super.dispatchTouchEvent(ev)
    }

    // TODO: Search feature
}
