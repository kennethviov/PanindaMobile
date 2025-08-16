package dev.komsay.basicapplication

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
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtUsername = findViewById<EditText>(R.id.editTextUsername)
        val txtPassword = findViewById<EditText>(R.id.editTextPassword)
        val btnLogin = findViewById<Button>(R.id.BtnLogin)

        val canBeNull: String


        btnLogin.setOnClickListener {
            if(txtUsername.text.toString() == "jampong" && txtPassword.text.toString() == "jampong123"){
                val intent = Intent(this, HomePage::class.java)
                intent.putExtra("USERNAME_KEY", txtUsername.text.toString())
                startActivity(intent)
            }else{
                Toast.makeText(this, "Bayot Ka", Toast.LENGTH_LONG).show()
            }
        }

    }
}