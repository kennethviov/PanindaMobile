package dev.komsay.panindamobile

import android.content.Intent
import android.os.Bundle
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
import dev.komsay.panindamobile.ui.components.NavigationBarManager
import dev.komsay.panindamobile.ui.fragments.LogoutConfirmation
import dev.komsay.panindamobile.ui.utils.DataHelper

class ProfilePage : AppCompatActivity() {

    private val profilePic: ImageView by lazy { findViewById(R.id.profilePicture) }
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            profilePic.setImageURI(it)
        }
    }

    private lateinit var app: Paninda
    private lateinit var dataHelper: DataHelper

    private lateinit var username: TextView
    private lateinit var changePicBtn: ImageButton
    private lateinit var changeNameBtn: ImageButton
    private lateinit var changeNameField: EditText
    private lateinit var changePassField: EditText
    private lateinit var editProfileBtn: Button
    private lateinit var logoutBtn: Button

    private var inEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        app = application as Paninda
        dataHelper = app.dataHelper

        val navigationBarManager = NavigationBarManager(this, findViewById(R.id.navbar))
        navigationBarManager.setup()

        username = findViewById(R.id.username)
        changePicBtn = findViewById(R.id.editProfilePic)
        changeNameBtn = findViewById(R.id.changeNameBtn)
        changeNameField = findViewById(R.id.changeNameField)
        changePassField = findViewById(R.id.changePasswordField)
        editProfileBtn = findViewById(R.id.editProfileBtn)
        logoutBtn = findViewById(R.id.logoutBtn)

        changePicBtn.setOnClickListener { changePicture(profilePic) }
        changeNameBtn.setOnClickListener { changeName() }
        logoutBtn.setOnClickListener { logout() }

        editProfileBtn.setOnClickListener {
            if (!inEditMode) { editProfile() }
            else { saveProfile() }
            inEditMode = !inEditMode
        }
    }

    // TODO add profile picture changing functionality
    private fun changePicture(profilePic: ImageView) {
        pickImageLauncher.launch("image/*")
    }

    private fun changeName() {
        val name = username.text.toString()
        username.visibility = View.GONE
        changeNameBtn.visibility = View.GONE
        changeNameField.visibility = View.VISIBLE
        changeNameField.setText(name)
    }

    /* TODO: refine profile editing and saving functionality
    *
    * */
    private fun editProfile() {
        changePicBtn.visibility = View.VISIBLE
        changeNameBtn.visibility = View.VISIBLE
        changePassField.visibility = View.VISIBLE
        editProfileBtn.text = "Save Profile"
        logoutBtn.alpha = 0.5f
        logoutBtn.isEnabled = false
    }

    private fun saveProfile() {

        val name = username.text

        changePicBtn.visibility = View.GONE
        changeNameBtn.visibility = View.GONE
        changePassField.visibility = View.GONE
        editProfileBtn.text = "Edit Profile"
        logoutBtn.alpha = 1f
        logoutBtn.isEnabled = true
    }

    private fun logout() {
        val dialog = LogoutConfirmation(this)

        dialog.setOnLogoutConfirmed {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
            finish()
        }

        dialog.show()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            currentFocus!!.clearFocus()

            val name = changeNameField.text.toString()
            changeNameField.visibility = View.GONE

            username.text = name
            username.visibility = View.VISIBLE
            changeNameBtn.visibility = View.VISIBLE
        }
        return super.dispatchTouchEvent(ev)
    }
}