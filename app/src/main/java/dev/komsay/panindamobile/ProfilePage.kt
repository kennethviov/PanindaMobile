package dev.komsay.panindamobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.komsay.panindamobile.ui.components.NavigationBarManager

class ProfilePage : AppCompatActivity() {

    private val profilePic: ImageView by lazy { findViewById(R.id.profilePicture) }
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            profilePic.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navigationBarManager = NavigationBarManager(this, findViewById(R.id.navbar))
        navigationBarManager.setup()

        val changePicBtn = findViewById<ImageButton>(R.id.editProfilePic)
        val changeNameBtn = findViewById<ImageButton>(R.id.changeNameBtn)
        val changePassBtn = findViewById<Button>(R.id.changePassBtn)
        val logoutBtn = findViewById<Button>(R.id.logoutBtn)

        changePicBtn.setOnClickListener { changePicture(profilePic) }
        changeNameBtn.setOnClickListener { changeName() }
        changePassBtn.setOnClickListener { changePassword() }
        logoutBtn.setOnClickListener { logout() }
    }

    /// TODO add profile picture changing functionality
    private fun changePicture(profilePic: ImageView) {
        pickImageLauncher.launch("image/*")
    }

    /// TODO add name changing functionality
    private fun changeName() {

    }

    /* TODO add password changing functionality
    *   - add new page to change password
    *   - add button to change password
    * */
    private fun changePassword() {

    }

    private fun logout() {
       val dialog = LogoutConfirmation(this)

        dialog.setOnLogoutConfirmed {
            startActivity(Intent(this, LogoutConfirmation::class.java))
            finish()
        }
        dialog.show();
    }
}