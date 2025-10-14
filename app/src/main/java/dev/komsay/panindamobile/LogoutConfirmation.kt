package dev.komsay.panindamobile

import android.app.Dialog
import android.content.Context
import android.widget.Button

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
            onLogoutConfirmed?.invoke()
            dialog.dismiss()
        }

        dialog.show()
    }
}
