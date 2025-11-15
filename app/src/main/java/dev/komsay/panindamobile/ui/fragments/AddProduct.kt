package dev.komsay.panindamobile.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.databinding.FragmentAddProductBinding
import dev.komsay.panindamobile.ui.data.Product

/**
 * DialogFragment version of the Add Product UI.
 * - Use `AddProductDialogFragment()` and then call `show()` via FragmentManager.
 * - Host can set an onProductAdded listener via `setOnProductAddedListener`.
 * - If you want to pre-populate for editing, call `populateForEdit(product)` before showing.
 */
class AddProductDialogFragment() : DialogFragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!

    // Listener to let host know when a product is added/updated
    private var onProductAdded: ((name: String, price: String, stock: String, category: String) -> Unit)? = null
    private var productToEditorDelete: Product? = null
    fun setOnProductAddedListener(listener: (String, String, String, String) -> Unit) {
        onProductAdded = listener
    }

    // Listener to let host know when a product is deleted
    private var onProductDeleted: ((product: Product) -> Unit)? = null
    private var productToDelete: Product? = null
    fun setOnProductDeletedListener(listener: (Product) -> Unit) {
        onProductDeleted = listener
    }


    // Keep the picked URI so result can be applied even if callback runs before/after view is ready
    private var pickedImageUri: Uri? = null

    // Register the image picker. DialogFragment inherits from Fragment which implements ActivityResultCaller.
    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            pickedImageUri = uri
            _binding?.productImage?.setImageURI(uri)
            if (uri == null) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "No image selected",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentAddProductBinding.inflate(LayoutInflater.from(requireContext()))

        // Apply any previously picked image
        pickedImageUri?.let { _binding?.productImage?.setImageURI(it) }

        setupUI()

        val builder = AlertDialog.Builder(requireContext())
            .setView(binding.root)

        // Create and return the dialog
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    private fun setupUI() {
        // Check if we are editing and populate fields
        productToEditorDelete?.let { product ->
            binding.btnConfirm.text = "Update"
            binding.deleteProduct.visibility = View.VISIBLE
            binding.productName.setText(product.name)
            binding.productPrice.setText(product.price.toString())
            binding.productStock.setText(product.stock.toString())
            binding.categories.setText(product.category)
            product.imageResId?.let { binding.productImage.setImageResource(it) }
        } ?: run {
            binding.btnConfirm.text = "Add"
            binding.deleteProduct.visibility = View.GONE
        }


        binding.deleteProduct.setOnClickListener {

            productToEditorDelete?.let { onProductDeleted?.invoke(it) }

            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                "Delete clicked (add code to delete product)",
                Snackbar.LENGTH_SHORT
            ).show()

            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnConfirm.setOnClickListener {
            val name = binding.productName.text.toString().trim()
            val price = binding.productPrice.text.toString().trim()
            val stock = binding.productStock.text.toString().trim()
            val category = binding.categories.text.toString().trim()

            // validate
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
            val message = if (productToEditorDelete != null) "Updated" else "Added"
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                "$message: $name",
                Snackbar.LENGTH_SHORT
            ).show()

            dismiss()
        }

        val categories = listOf("Beverages", "Snacks", "Produce", "Dairy")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categories)
        binding.categories.setAdapter(adapter)

        // Image click launches image picker
        binding.productImage.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }
    }

    /**
     * If host wants to edit an existing product, call this BEFORE showing the dialog.
     */
    fun populateForEdit(sender: Product) {
        productToEditorDelete = sender
    }

    /**
     * Allow host to programmatically set image URI (if image is chosen elsewhere)
     */
    fun setPickedImageUri(uri: Uri) {
        pickedImageUri = uri
        _binding?.productImage?.setImageURI(uri)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}