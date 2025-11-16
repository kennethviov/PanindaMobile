package dev.komsay.panindamobile

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import androidx.annotation.RequiresApi
import dev.komsay.panindamobile.ui.components.CategoryComponent
import dev.komsay.panindamobile.ui.components.ProductSellerComponent
import dev.komsay.panindamobile.ui.data.Product
import dev.komsay.panindamobile.ui.components.TopSellingProductComponent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import dev.komsay.panindamobile.ui.components.CartItemComponent
import dev.komsay.panindamobile.ui.components.NavigationBarManager
import dev.komsay.panindamobile.ui.data.Sales
import com.google.android.material.snackbar.Snackbar
import dev.komsay.panindamobile.ui.data.CartItem
import dev.komsay.panindamobile.ui.utils.DataHelper

class HomePage : AppCompatActivity() {

    private var products: List<Product> = mutableListOf()
    private val cart: MutableList<CartItem> = mutableListOf()
    private lateinit var container: LinearLayout

    private lateinit var app: Paninda
    private lateinit var dataHelper: DataHelper

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
        val navigationBarManager = NavigationBarManager(this, findViewById(R.id.navbar))
        navigationBarManager.setup()

        // Home Page Components
        container = findViewById(R.id.productSellerContainer)
        val receivedUsername = intent.getStringExtra("USERNAME_KEY")
        val userTxtView = findViewById<TextView>(R.id.userTextView)
        val dateTime = findViewById<TextView>(R.id.dateTimeView)
        val cartCancel = findViewById<Button>(R.id.btn_cancel)
        val cartSell = findViewById<Button>(R.id.btn_sell)

        /*
        *
        *  MOCK DATA
        *
        * */
        app = application as Paninda
        dataHelper = app.dataHelper

        // Home Page  Greeting
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
        val formattedDateTime = currentDateTime.format(formatter)

        userTxtView.text = "Hello, $receivedUsername"
        dateTime.text =formattedDateTime.toString()


        val categories = dataHelper.getAllCategories()

        // products
        loadProductsFromDB()
        updateProductUI(this.products)

        // top seller
        loadTopSellingProducts(this.products)

        // categories
        for (cat in categories) {
            val catComp = CategoryComponent(findViewById(R.id.categorySlider))
            catComp.bind(cat) { selectedCategory ->
                handleCategoryClick(selectedCategory) }
        }

        // SetUpCartBtn
        setUpCartBtn(cartCancel, cartSell)

        // SetUpCartUI
        updateCartUI()

    }

    @SuppressLint("DefaultLocale")
    private fun handleSellClick(product: Product, quantity: Int) {

        if(quantity <= 0) {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Quantity must be greater then 0",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        if (quantity > product.stock) {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Not enough stock for ${product.name}",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        if (cart.any { it.productName == product.name }) {
            val cartItem = cart.find { it.productName == product.name }!!

            if (cartItem.productQuantity + quantity > product.stock) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Cannot add more than stock",
                    Snackbar.LENGTH_SHORT
                ).show()
                return
            }

            cartItem.productQuantity += quantity
            updateCartUI()
            return
        }

        val cartItem = CartItem(
            product.imageResId,
            product.name,
            product.price,
            quantity
        )
        cart.add(cartItem)
        toggleCartVisibility("visible")
        updateCartUI()
    }

    private var currCat: String = ""

    fun handleCategoryClick(category: String) {
        if (currCat == category) {
            updateProductUI(this.products)
            loadTopSellingProducts(this.products)
            currCat = ""
            return
        }

        val categorizedProducts = products.filter { it.category == category }
        updateProductUI(categorizedProducts)
        loadTopSellingProducts(categorizedProducts)
        currCat = category
    }

    fun updateProductUI(products: List<Product>) {
        container.removeAllViews()

        for (product in products) {
            val component = ProductSellerComponent(container)
            component.bind(product) { selectedProduct, quantity ->
                handleSellClick(selectedProduct, quantity)
            }
        }
    }

    fun loadTopSellingProducts(products: List<Product>) {
        val topSellCont = findViewById<LinearLayout>(R.id.view_top_seller)
        topSellCont.removeAllViews()
        val topSellComp = TopSellingProductComponent(topSellCont)
        topSellComp.bind(products.find { it -> it.unitSold == products.maxByOrNull { it.unitSold }?.unitSold }!!, totalUnitSold(products))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpCartBtn(cancelBtn: Button, sellBtn: Button) {
        cancelBtn.setOnClickListener {
            cart.clear()
            toggleCartVisibility("gone")
        }

        sellBtn.setOnClickListener {

            val numOfSales = dataHelper.getAllSales().size

            val sale = Sales(
                "100$numOfSales",
                LocalDateTime.now().toString(),
                ArrayList(cart))

            dataHelper.addSale(sale)

            for (item in cart) {
                val product = products.find { it.name == item.productName }

                if (product != null) {

                    product.stock -= item.productQuantity

                    if (product.stock < 0) {
                        product.stock = 0
                    }

                    product.unitSold += item.productQuantity

                    dataHelper.updateProduct(product)
                }
            }

            cart.clear()
            loadProductsFromDB()
            updateProductUI(this.products)
            toggleCartVisibility("gone")
        }
    }

    private fun updateCartUI() {
        val sales = cart.sumOf { it.productPrice * it.productQuantity }
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
        products = dataHelper.getAllProducts()
    }

    private fun totalUnitSold(products: List<Product>): Int {
        return products.sumOf { it.unitSold }
    }

    // TODO: Search feature
}
