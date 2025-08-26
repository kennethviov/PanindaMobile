package dev.komsay.panindamobile.ui.components

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import dev.komsay.panindamobile.R
import dev.komsay.panindamobile.ui.data.Category

class CategoryComponent {

    private val view: View
    private val categoryname: TextView

    constructor(container: LinearLayout) {

        view = LayoutInflater.from(container.context)
            .inflate(R.layout.category, container, false)

        container.addView(view)

        categoryname = view.findViewById(R.id.categoryName)
    }

    fun bind(category: Category) {
        categoryname.text = category.name
    }

}