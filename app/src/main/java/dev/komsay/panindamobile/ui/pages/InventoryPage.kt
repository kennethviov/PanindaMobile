package dev.komsay.panindamobile.ui.pages

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import dev.komsay.panindamobile.backend.dto.CategoryDTO
import dev.komsay.panindamobile.backend.dto.ProductsDTO
import dev.komsay.panindamobile.backend.network.RetrofitClient
import dev.komsay.panindamobile.ui.components.NavigationBarManager
import dev.komsay.panindamobile.ui.components.ProductInventoryComponent
import kotlinx.coroutines.launch

class InventoryPage : AppCompatActivity() {

    private lateinit var app: Paninda
    private lateinit var productContainer: LinearLayout
    private lateinit var categorySlider: LinearLayout
    private var products: List<ProductsDTO> = emptyList()
    private var categories: List<CategoryDTO> = emptyList()

    @RequiresApi(Build.VERSION_CODES.O)
    private val addOrModifyProductLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // Refresh data if a product was added or modified
            loadProductsFromDB()
            loadCategoriesFromDB()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inventory_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.inventoryPage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navigationBarManager = NavigationBarManager(this, findViewById(R.id.navbar)).apply {
            setup()
            highlightActivePage(R.id.indicatorInventory)
        }

        productContainer = findViewById(R.id.productInventoryContainer)
        categorySlider = findViewById(R.id.categorySlider)
        val addBtn = findViewById<ImageButton>(R.id.addBtn)

        // load mock data
        loadProductsFromDB()
        loadCategoriesFromDB()

        // set up ui
        handleAddBtnClick(addBtn)
        refreshProductUI()
        refreshCategoryUI(this.categories)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun handleAddBtnClick(addBtn: ImageButton) {
        addBtn.setOnClickListener {
            val intent = Intent(this, AddOrModifyProductPage::class.java)
            addOrModifyProductLauncher.launch(intent)
        }
    }

    private var currCat: Button? = null
    private var lastCat: Button? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun handleCategoryClick(category: Button) {
        if (currCat == category) {
            refreshProductUI(this.products)
            category.backgroundTintList = ContextCompat.getColorStateList(this@InventoryPage, R.color.categoryColor)
            currCat = null
            return
        }

        lastCat?.backgroundTintList = ContextCompat.getColorStateList(this@InventoryPage, R.color.categoryColor)

        val categorizedProducts = products.filter { it.categoryName == category.text }
        refreshProductUI(categorizedProducts)
        category.backgroundTintList = ContextCompat.getColorStateList(this@InventoryPage, R.color.selectedCategoryColor)
        currCat = category
        lastCat = currCat
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshProductUI(products: List<ProductsDTO> = this.products) {
        productContainer.removeAllViews()

        for (product in products) {
            val component = ProductInventoryComponent(productContainer, this)
            component.bind(product, addOrModifyProductLauncher) {
                // This is the callback for when a product is updated.
                refreshProductUI()
            }
        }
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
                typeface = ResourcesCompat.getFont(this@InventoryPage, R.font.inter_regular)
                setTextColor(ContextCompat.getColor(this@InventoryPage, R.color.black))
                isAllCaps = false

                background = ContextCompat.getDrawable(this@InventoryPage, R.drawable.bg_category)
                backgroundTintList = ContextCompat.getColorStateList(this@InventoryPage, R.color.categoryColor)

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadProductsFromDB() {
        val productsApi = RetrofitClient.getProductsApi(this)

        lifecycleScope.launch {
            try {
                val fetchedProducts = productsApi.getAllProducts()
                Log.d("Inventory: loadProductsFromDB()", "Products fetched: $fetchedProducts")


                products = fetchedProducts

                refreshProductUI()

            } catch (e: Exception) {
                Log.e("Inventory", "Failed to load products: ${e.message}")
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
                Log.d("Inventory: loadCategoriesFromDB()", "Categories fetched: $fetchedCategories")

                categories = fetchedCategories

                refreshCategoryUI(categories)

            } catch (e: Exception) {
                Log.e("Inventory", "Failed to load categories: ${e.message}")
                e.printStackTrace()

                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Failed to load categories: ${e.message}",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            currentFocus!!.clearFocus()
        }
        return super.dispatchTouchEvent(ev)
    }

    /* TODO:
    *   - Search feature
    *   - Convert main product container and cart container to
    *           RecyclerView for better performance; great for large amounts of data
    *   - SwipeRefreshLayout to refresh
    * */
}
