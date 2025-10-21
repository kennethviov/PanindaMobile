package dev.komsay.panindamobile.ui.fragments

import android.R
import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import dev.komsay.panindamobile.databinding.FragmentAddProductBinding
import dev.komsay.panindamobile.ui.data.Product

class AddProduct(private val context: Context) {

    private var onProductAdded: ((String, String, String, String) -> Unit)? = null

    fun setOnProductAddedListener(listener: (String, String, String, String) -> Unit) {
        onProductAdded = listener
    }

    fun show(sender: Product?) {
        val dialog = Dialog(context)
        val binding = FragmentAddProductBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)

        binding.btnConfirm.text = if (sender == null) "Add" else "Update"
        binding.deleteProduct.visibility = View.GONE

        if (sender != null) {
            binding.deleteProduct.visibility = View.VISIBLE

            binding.productName.setText(sender.name)
            binding.productPrice.setText(sender.getFormattedPrice(sender.price))
            binding.productStock.setText(sender.stock.toString())
            binding.categories.setText(sender.category)
        }

        binding.deleteProduct.setOnClickListener {
            Toast.makeText(context, "Delete clicked (add code to delete product)", Toast.LENGTH_SHORT).show()
        }


        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnConfirm.setOnClickListener {
            val name = binding.productName.text.toString().trim()
            val price = binding.productPrice.text.toString().trim()
            val stock = binding.productStock.text.toString().trim()
            val category = binding.categories.text.toString().trim()

            //validate
            if (name.isEmpty()) {
                binding.productName.error = "Product name is required"
                binding.productName.requestFocus()
                return@setOnClickListener
            }
            if (price.isEmpty()) {
                binding.productPrice.error = "Product price is required"
                binding.productPrice.requestFocus()
                return@setOnClickListener
            }
            if (stock.isEmpty()) {
                binding.productStock.error = "Product stock is required"
                binding.productStock.requestFocus()
                return@setOnClickListener
            }

            onProductAdded?.invoke(name, price, stock, category)
            Toast.makeText(context, "Saved: $name", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        val categories = listOf("Beverages", "Snacks", "Produce", "Dairy")
        val adapter = ArrayAdapter(context, R.layout.simple_list_item_1, categories)
        binding.categories.setAdapter(adapter)

        // TO-DO Click image to open camera or gallery
        binding.productImage.setOnClickListener {
            Toast.makeText(context, "Image clicked (add code to pick image", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }
}