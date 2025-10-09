package dev.komsay.panindamobile

import android.net.Uri
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
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
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profilePage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // animated background
        val rootLayout = findViewById<ConstraintLayout>(R.id.profilePage)
        rootLayout.setBackgroundResource(R.drawable.animation_bg)
        val animationDrawable = rootLayout.background as AnimationDrawable
        animationDrawable.start()



        profilePicture = findViewById(R.id.profilePicture)
        changePicBtn = findViewById(R.id.changePicBtn)
        changePicBtn.setOnClickListener {
            pickImageLauncher.launch("image/*") // open gallery
        }


        // Logout function
        val logoutBtn = findViewById<AppCompatButton>(R.id.logoutBtn)

        logoutBtn.setOnClickListener {
            val logoutDialog = logoutConfirmation(this)
            logoutDialog.show()
        }


        // navbar setup
        val navHome = findViewById<ImageButton>(R.id.navHome)
        val navSales = findViewById<ImageButton>(R.id.navSales)
        val navProfile = findViewById<ImageButton>(R.id.navProfile)
        val navInventory = findViewById<ImageButton>(R.id.navBox)
        val navStats = findViewById<ImageButton>(R.id.navStats)

        navHome.setOnClickListener {
            if (this::class.java != HomePage::class.java) {
                startActivity(Intent(this, HomePage::class.java))
                overridePendingTransition(0, 0)
            }
        }

        navSales.setOnClickListener {
            if (this::class.java != SalesPage::class.java) {
                startActivity(Intent(this, SalesPage::class.java))
                overridePendingTransition(0, 0)
            }
        }

        navProfile.setOnClickListener {
            if (this::class.java != ProfilePage::class.java) {
                startActivity(Intent(this, ProfilePage::class.java))
                overridePendingTransition(0, 0)
            }
        }

        navInventory.setOnClickListener {
            if (this::class.java != InventoryPage::class.java) {
                startActivity(Intent(this, InventoryPage::class.java))
                overridePendingTransition(0, 0)
            }
        }

        navStats.setOnClickListener {
            if (this::class.java != AnalyticsPage::class.java) {
                startActivity(Intent(this, AnalyticsPage::class.java))
                overridePendingTransition(0, 0)
            }
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
