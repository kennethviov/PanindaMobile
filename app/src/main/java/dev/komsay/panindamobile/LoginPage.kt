package dev.komsay.panindamobile

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.EditText
import android.widget.Toast
import android.widget.Button
class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginPage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtUsername = findViewById<EditText>(R.id.editTextUsername)
        val txtPassword = findViewById<EditText>(R.id.editTextPassword)
        val btnLogin = findViewById<Button>(R.id.BtnLogin)
        val btnSignUp = findViewById<Button>(R.id.BtnSignUp)
        val canBeNull: String


        btnLogin.setOnClickListener {
            if(txtUsername.text.toString() == "admin" && txtPassword.text.toString() == "admin"){
                val intent = Intent(this, HomePage::class.java)
                intent.putExtra("USERNAME_KEY", txtUsername.text.toString())
                startActivity(intent)

                // to prevent going back to login kenesu
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "Bayot Ka", Toast.LENGTH_LONG).show()
            }
        }


        btnSignUp.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
        }


    }
}