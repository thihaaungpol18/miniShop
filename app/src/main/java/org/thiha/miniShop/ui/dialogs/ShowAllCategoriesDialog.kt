package org.thiha.miniShop.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.DialogFragment
import org.thiha.miniShop.R
import org.thiha.miniShop.utils.Utils

class ShowAllCategoriesDialog : DialogFragment() {
    interface SelectCategory {
        fun onSelectCategoryResult(category: String?)
    }

    private var selectCategory: SelectCategory? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_show_all_categories, null)
        val builder = AlertDialog.Builder(activity)
                .setView(view)
                .setTitle("All Categories")
        val listView = view.findViewById<View>(R.id.categoriesListView) as ListView
        val utils = Utils(activity)
        val categories = utils.allCategories
        val adapter = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, categories)
        listView.adapter = adapter
        try {
            selectCategory = activity as SelectCategory?
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
        listView.onItemClickListener = OnItemClickListener { parent, view, position, id -> selectCategory!!.onSelectCategoryResult(categories[position]) }
        return builder.create()
    }

    companion object {
        private const val TAG = "ShowAllCategoriesDialog"
    }
}