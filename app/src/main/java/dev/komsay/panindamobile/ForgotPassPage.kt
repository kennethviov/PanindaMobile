package dev.komsay.panindamobile

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class ForgotPassPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpass_page)
        val rootView = findViewById<View>(android.R.id.content)

        val username = findViewById<EditText>(R.id.editTextUsername)
        val newpwd = findViewById<EditText>(R.id.editTextnewPassword)
        val confirmpwd = findViewById<EditText>(R.id.editTextConfirmPassword)
        val btnConfirm = findViewById<Button>(R.id.btnConfirm)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        btnConfirm.setOnClickListener {
            val user = username.text.toString()
            val newpwd = newpwd.text.toString()
            val cpwd = confirmpwd.text.toString()

            if (user.isEmpty() || newpwd.isEmpty() || cpwd.isEmpty()){
                Snackbar.make(rootView, "Please fill all fields", Snackbar.LENGTH_SHORT).show()
            }else if (newpwd != cpwd){
                Snackbar.make(rootView, "Password do not match please try again", Snackbar.LENGTH_SHORT).show()
            }else{
                Snackbar.make(rootView, "Password Successfully Changed for $user", Snackbar.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            val user = username.text.toString()
            val newpwd = newpwd.text.toString()
            val cpwd = confirmpwd.text.toString()
            Snackbar.make(rootView, "Cancelled", Snackbar.LENGTH_SHORT).show()
        }
    }
}
