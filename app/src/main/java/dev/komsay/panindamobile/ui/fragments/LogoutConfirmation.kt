package dev.komsay.panindamobile.ui.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.widget.Button
import dev.komsay.panindamobile.ui.pages.LoginPage
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.backend.network.RetrofitClient
import dev.komsay.panindamobile.backend.service.SharedPrefManager

class LogoutConfirmation(private val context: Context) {

    private var onLogoutConfirmed: (() -> Unit)? = null

    fun setOnLogoutConfirmed(listener: () -> Unit) {
        onLogoutConfirmed = listener
    }

    fun show() {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.fragment_logout_confirmation)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val cancelBtn = dialog.findViewById<Button>(R.id.btnCancel)
        val logoutBtn = dialog.findViewById<Button>(R.id.btnLogout)

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        logoutBtn.setOnClickListener {

            SharedPrefManager.clear(context)

            RetrofitClient.clear()

            val intent = Intent(context, LoginPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
        dialog.show()
    }
}