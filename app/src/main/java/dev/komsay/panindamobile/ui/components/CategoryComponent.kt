package dev.komsay.panindamobile.ui.components

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import dev.komsay.panindamobile.R

class CategoryComponent {

    private val view: View
    private val categoryName: TextView

    constructor(container: LinearLayout) {

        view = LayoutInflater.from(container.context)
            .inflate(R.layout.component_category, container, false)

        container.addView(view)

        categoryName = view.findViewById(R.id.categoryName)
    }

    fun bind(category: String) {
        categoryName.text = category
    }

}