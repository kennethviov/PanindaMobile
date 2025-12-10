package dev.komsay.panindamobile.ui.pages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import dev.komsay.panindamobile.Paninda
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.backend.dto.LoginUsersDTO
import dev.komsay.panindamobile.backend.dto.UpdatePasswordDTO
import dev.komsay.panindamobile.backend.dto.UserDTO
import dev.komsay.panindamobile.backend.network.RetrofitClient
import dev.komsay.panindamobile.backend.service.SharedPrefManager
import dev.komsay.panindamobile.ui.components.NavigationBarManager
import dev.komsay.panindamobile.ui.fragments.LogoutConfirmation
import okhttp3.Callback
import retrofit2.Call
import retrofit2.Response

class ProfilePage : AppCompatActivity() {

    private val profilePic: ImageView by lazy { findViewById(R.id.profilePicture) }
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            profilePic.setImageURI(it)
        }
    }

    private lateinit var username: TextView
    private lateinit var logoutBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Navigation bar
        val navigationBarManager = NavigationBarManager(this, findViewById(R.id.navbar)).apply {
            setup()
            highlightActivePage(R.id.indicatorProfile)
        }

        // Initialize views
        username = findViewById(R.id.username)
        logoutBtn = findViewById(R.id.logoutBtn)

        // Set up UI

        logoutBtn.setOnClickListener { logout() }

        username.text = SharedPrefManager.getUsername(this)
    }


    private fun logout() {
        val dialog = LogoutConfirmation(this)

        dialog.setOnLogoutConfirmed {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
            finish()
        }

        dialog.show()
        SharedPrefManager.clear(this)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            currentFocus!!.clearFocus()
        }
        return super.dispatchTouchEvent(ev)
    }
}