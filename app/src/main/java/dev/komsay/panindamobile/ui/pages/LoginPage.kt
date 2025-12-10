package dev.komsay.panindamobile.ui.pages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.EditText
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.backend.dto.LoginUsersDTO
import dev.komsay.panindamobile.backend.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import dev.komsay.panindamobile.backend.dto.LoginResponseDTO
import dev.komsay.panindamobile.backend.service.SharedPrefManager


class LoginPage : AppCompatActivity() {

    private lateinit var txtUsername: EditText
    private lateinit var txtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button
    private lateinit var linkForgotPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if user is already logged in
//        if (SharedPrefManager.isLoggedIn(this)) {
//            // User is already logged in, go to HomePage
//            val intent = Intent(this, HomePage::class.java)
//            startActivity(intent)
//            finish()
//            return
//        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginPage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtUsername = findViewById(R.id.editTextUsername)
        txtPassword = findViewById(R.id.editTextPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignUp = findViewById(R.id.btnSignUp)
        linkForgotPassword = findViewById(R.id.forgotPass)

        btnLogin.setOnClickListener {
            loginButtonClick()
        }

        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignupPage::class.java)
            startActivity(intent)
        }

        linkForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPassPage::class.java)
            startActivity(intent)
        }
    }

    private fun loginButtonClick() {
        val user = txtUsername.text.toString().trim()
        val pwd = txtPassword.text.toString().trim()

        if (user.isEmpty() || pwd.isEmpty()) {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Please fill all fields",
                Snackbar.LENGTH_SHORT
            ).show()
            if (user.isEmpty()) txtUsername.requestFocus()
            if (pwd.isEmpty()) txtPassword.requestFocus()
            return
        }

        SharedPrefManager.clear(this)

        RetrofitClient.clear()

        val loginDto = LoginUsersDTO(username = user, password = pwd)

        RetrofitClient.getApi(this).loginUser(loginDto).enqueue(object : Callback<LoginResponseDTO> {
            override fun onResponse(call: Call<LoginResponseDTO>, response: Response<LoginResponseDTO>) {

                Log.d("LoginPage", "Response code: ${response.code()}")
                Log.d("LoginPage", "Response body: ${response.body()}")
                Log.d("LoginPage", "Raw response: ${response.raw()}")

                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!

                    Log.d("LoginPage", "Token received: ${loginResponse.token}")
                    Log.d("LoginPage", "Username: ${loginResponse.username}")

                    val saved = SharedPrefManager.saveLoginData(
                        this@LoginPage,
                        loginResponse.token,
                        loginResponse.username
                    )

                    Log.d("LoginPage", "Login data saved: $saved")

                    val retrievedToken = SharedPrefManager.getToken(this@LoginPage)
                    val retrievedUsername = SharedPrefManager.getUsername(this@LoginPage)
                    Log.d("LoginPage", "Retrieved token: $retrievedToken")
                    Log.d("LoginPage", "Retrieved username: $retrievedUsername")

                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Welcome ${loginResponse.username}",
                        Snackbar.LENGTH_LONG
                    ).show()

                    val intent = Intent(this@LoginPage, HomePage::class.java)
                    intent.putExtra("USERNAME_KEY", loginResponse.username)
                    startActivity(intent)
                    finish()
                } else {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Invalid username or password",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<LoginResponseDTO>, t: Throwable) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Error: ${t.message}",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })
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
