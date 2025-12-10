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
import dev.komsay.panindamobile.backend.dto.UpdatePasswordDTO
import dev.komsay.panindamobile.backend.dto.UserDTO
import dev.komsay.panindamobile.backend.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPassPage : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnConfirm: Button
    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_pass_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etUsername = findViewById(R.id.editTextUsername)
        etNewPassword = findViewById(R.id.editNewPassword)
        etConfirmPassword = findViewById(R.id.editTextConfirmPassword)
        btnConfirm = findViewById(R.id.btnConfirm)
        btnCancel = findViewById(R.id.btnCancel)

        btnConfirm.setOnClickListener {
            confirmButtonClick()
        }

        btnCancel.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }

    private fun confirmButtonClick() {
        val user = etUsername.text.toString().trim()
        val newpass = etNewPassword.text.toString().trim()
        val cpwd = etConfirmPassword.text.toString().trim()

        if (user.isEmpty() || newpass.isEmpty() || cpwd.isEmpty()) {
            if (user.isEmpty()) etUsername.requestFocus()
            else if (newpass.isEmpty()) etNewPassword.requestFocus()
            else if (cpwd.isEmpty()) etConfirmPassword.requestFocus()

            Snackbar.make(
                findViewById(android.R.id.content),
                "Please fill all fields",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        if (newpass != cpwd) {
            etConfirmPassword.requestFocus()
            Snackbar.make(
                findViewById(android.R.id.content),
                "Passwords do not match, please try again",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        val updatedPassword = UpdatePasswordDTO(username = user, newPassword = newpass)

        RetrofitClient.getApi(this).updatePassword(updatedPassword)
            .enqueue(object : Callback<UserDTO> {

                override fun onResponse(
                    call: Call<UserDTO?>,
                    response: Response<UserDTO?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val result = response.body()!!

                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Password successfully changed for ${result.username}",
                            Snackbar.LENGTH_LONG
                        ).show()

                        // Redirect to LoginPage
                        val intent = Intent(this@ForgotPassPage, LoginPage::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("ForgotPassPage", "Update failed: ${response.code()} - $errorBody")

                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Password update failed: ${response.code()}",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<UserDTO?>,
                    t: Throwable
                ) {
                    Log.e("ForgotPassPage", "Error: ${t.message}", t)

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