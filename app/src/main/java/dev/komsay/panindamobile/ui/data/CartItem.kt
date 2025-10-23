package dev.komsay.panindamobile.ui.data

import android.widget.ImageView

data class CartItem (

    val imageResId: Int?,
    val productName: String,
    val productPrice: Double,
    var productQuantity: Int,

)