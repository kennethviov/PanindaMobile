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
import dev.komsay.panindamobile.dto.LoginUsersDTO
import dev.komsay.panindamobile.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import dev.komsay.panindamobile.dto.LoginResponse
import dev.komsay.panindamobile.Service.SharedPrefManager


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
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)

        btnLogin.setOnClickListener {
            val user = txtUsername.text.toString().trim()
            val pwd = txtPassword.text.toString().trim()

            if (user.isEmpty() || pwd.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loginDto = LoginUsersDTO(username = user, password = pwd)

            RetrofitClient.getApi(this).loginUser(loginDto).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val loginResponse = response.body()!!

                        // Save token
                        SharedPrefManager.saveToken(this@LoginPage, loginResponse.token)

                        Toast.makeText(this@LoginPage, "Welcome ${loginResponse.username}", Toast.LENGTH_LONG).show()

                        val intent = Intent(this@LoginPage, HomePage::class.java)
                        intent.putExtra("USERNAME_KEY", loginResponse.username)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginPage, "Invalid username or password", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginPage, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }

        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignupPage::class.java)
            startActivity(intent)
        }
    }
}
