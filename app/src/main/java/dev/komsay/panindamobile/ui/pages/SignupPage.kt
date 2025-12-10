package dev.komsay.panindamobile.ui.pages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.backend.dto.RegisterUsersDTO
import dev.komsay.panindamobile.backend.dto.UserDTO
import dev.komsay.panindamobile.backend.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup_page)
        val rootView = findViewById<View>(android.R.id.content)
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
            val user = username.text.toString().trim()
            val pwd = password.text.toString().trim()
            val cpwd = confirmpwd.text.toString().trim()

            if (user.isEmpty() || pwd.isEmpty() || cpwd.isEmpty()) {
                Snackbar.make(
                    rootView, 
                    "Please fill all fields",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (pwd != cpwd) {
                Snackbar.make(
                    rootView, 
                    "Passwords do not match",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Send data to Spring Boot API
            val registerDto = RegisterUsersDTO(username = user, password = pwd)
            RetrofitClient.getApi(this).registerUser(registerDto).enqueue(object : Callback<UserDTO> {
                override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                    if (response.isSuccessful) {
                        val newUser = response.body()
                        Snackbar.make(
                            rootView,
                            "Account created for ${newUser?.username}",
                            Snackbar.LENGTH_LONG
                        ).show()

                        // Redirect to LoginPage
                        startActivity(Intent(this@SignupPage, LoginPage::class.java))
                        finish()
                    } else {
                        Snackbar.make(
                            rootView,
                            "Registration failed: ${response.code()}",
                            Snackbar.LENGTH_LONG
                        ).show()
                        Log.e("SignupPage", "Registration failed: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                    Snackbar.make(
                        rootView,
                        "Error: ${t.message}",
                        Snackbar.LENGTH_LONG
                    ).show()

                    Log.e("SignupPage", "Error: ${t.message}")
                }
            })

        }

        btnCancel.setOnClickListener {
            Snackbar.make(rootView, "Cancelled", Snackbar.LENGTH_SHORT).show()
            val intent = Intent(this, LoginPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
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
