package dev.komsay.panindamobile.ui.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import dev.komsay.panindamobile.backend.dto.SalesDTO
import dev.komsay.panindamobile.databinding.FragmentSalesDetailsBinding
import dev.komsay.panindamobile.ui.components.ProductInventoryComponent
import dev.komsay.panindamobile.ui.data.Sale
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SalesDetails(private val context: Context) {

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    fun show(sales: SalesDTO) {
        val dialog = Dialog(context)
        val binding = FragmentSalesDetailsBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // datetime format
        val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
        val dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
        val dateTime = LocalDateTime.parse(sales.salesDate.toString())

        // display infos
        binding.txtTimeDate.text = dateTime.format(timeFormatter) + ", " + dateTime.format(dateFormatter)
        binding.txtSalesId.text = sales.id.toString()
        binding.txtSalesTotal.text = String.format("₱%.2f", sales.totalPrice)

        // dynamic container
        val container = binding.salesDetailsContainer
        val scroller = binding.scroller
        val layoutParam = scroller.layoutParams
        val dummyComp = ProductInventoryComponent(container, context)

        val compHeight = dummyComp.getView().layoutParams.height

        layoutParam.height = setScrollViewHeight(compHeight, sales.items.size)
        scroller.layoutParams = layoutParam

        for (item in sales.items) {
            val comp = ProductInventoryComponent(container, context)
            comp.bind(item)
        }

        binding.btnOk.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setScrollViewHeight(componentHeight: Int, size: Int): Int {
        val actualHeight: Double = (componentHeight + 16).toDouble() * (size).toDouble()

        val maxHeight: Double = (componentHeight + 16).toDouble() * 3.5

        if (actualHeight > maxHeight) {
            return maxHeight.toInt()
        }
        return actualHeight.toInt()
    }

    /* TODO:
    *   - Convert main product container and cart container to
    *           RecyclerView for better performance; great for large amounts of data
    *   - SwipeRefreshLayout to refresh
    * */
}
