package dev.komsay.basicapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ForgotPassPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpass_page)

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
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }else if (newpwd != cpwd){
                Toast.makeText(this, "Password do not match please try again", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Password Successfully Changed for $user", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            val user = username.text.toString()
            val newpwd = newpwd.text.toString()
            val cpwd = confirmpwd.text.toString()
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}
