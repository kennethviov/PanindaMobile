package dev.komsay.panindamobile

import android.app.Application
import dev.komsay.panindamobile.ui.utils.DataHelper

class Paninda : Application() {
    lateinit var dataHelper : DataHelper
        private set

    override fun onCreate() {
        super.onCreate()
        dataHelper = DataHelper(this)
    }
}