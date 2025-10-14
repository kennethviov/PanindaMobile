package dev.komsay.panindamobile

import android.net.Uri
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProfilePage : AppCompatActivity() {

    private lateinit var profilePicture: ImageView
    private lateinit var changePicBtn: ImageButton

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            profilePicture.setImageURI(it)
            // Save URI string to SharedPreferences
            val prefs = getSharedPreferences("user_profile", MODE_PRIVATE)
            prefs.edit().putString("profile_image_uri", it.toString()).apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_page)

        // Initialize views FIRST
        val rootLayout = findViewById<ConstraintLayout>(R.id.profilePage)
        profilePicture = findViewById(R.id.profilePicture)
        changePicBtn = findViewById(R.id.changePicBtn)
        val logoutBtn = findViewById<AppCompatButton>(R.id.logoutBtn)

        // Apply system insets
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Load saved profile image (if any)
        val prefs = getSharedPreferences("user_profile", MODE_PRIVATE)
        val savedUri = prefs.getString("profile_image_uri", null)
        if (savedUri != null) {
            try {
                profilePicture.setImageURI(Uri.parse(savedUri))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Animated background
        rootLayout.setBackgroundResource(R.drawable.animation_bg)
        val animationDrawable = rootLayout.background as AnimationDrawable
        animationDrawable.start()

        // Change picture
        changePicBtn.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // Logout button
        logoutBtn.setOnClickListener {
            val logoutDialog = LogoutConfirmation(this)
            logoutDialog.setOnLogoutConfirmed {
                prefs.edit().clear().apply()
                startActivity(Intent(this, LoginPage::class.java))
                finish()
            }
            logoutDialog.show()
        }

        // Navbar setup (✅ FIXED VERSION)
        setupNavBar()
    }

    private fun setupNavBar() {
        // ✅ Access buttons inside the included layout
        val navBar = findViewById<View>(R.id.navbar)
        val navHome = navBar.findViewById<ImageButton>(R.id.navHome)
        val navSales = navBar.findViewById<ImageButton>(R.id.navSales)
        val navProfile = navBar.findViewById<ImageButton>(R.id.navProfile)
        val navInventory = navBar.findViewById<ImageButton>(R.id.navBox)
        val navStats = navBar.findViewById<ImageButton>(R.id.navStats)

        navHome.setOnClickListener {
            startActivity(Intent(this, HomePage::class.java))
            overridePendingTransition(0, 0)
        }

        navSales.setOnClickListener {
            startActivity(Intent(this, SalesPage::class.java))
            overridePendingTransition(0, 0)
        }

        // Already on ProfilePage
        navProfile.setOnClickListener { }

        navInventory.setOnClickListener {
            startActivity(Intent(this, InventoryPage::class.java))
            overridePendingTransition(0, 0)
        }

        navStats.setOnClickListener {
            startActivity(Intent(this, AnalyticsPage::class.java))
            overridePendingTransition(0, 0)
        }
    }

    override fun onResume() {
        super.onResume()
        val rootLayout = findViewById<ConstraintLayout>(R.id.profilePage)
        (rootLayout.background as AnimationDrawable).start()
    }

    override fun onPause() {
        super.onPause()
        val rootLayout = findViewById<ConstraintLayout>(R.id.profilePage)
        (rootLayout.background as AnimationDrawable).stop()
    }
}
