package dev.komsay.panindamobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignupPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signUpPage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val username = findViewById<EditText>(R.id.editTextUsername)
        val password = findViewById<EditText>(R.id.editTextPassword)
        val confirmpwd = findViewById<EditText>(R.id.editTextConfirmPassword)
        val btnConfirm = findViewById<Button>(R.id.btnConfirm)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        btnConfirm.setOnClickListener {
            val user = username.text.toString()
            val pwd = password.text.toString()
            val cpwd = confirmpwd.text.toString()

            if (user.isEmpty() || pwd.isEmpty() || cpwd.isEmpty()){
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }else if (pwd != cpwd){
                Toast.makeText(this, "Password do not match please try again", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Account Created for $user", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            val user = username.text.toString()
            val pwd = password.text.toString()
            val cpwd = confirmpwd.text.toString()
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }

    }
}