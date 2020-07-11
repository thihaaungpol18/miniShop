package org.thiha.miniShop.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.thiha.miniShop.R
import org.thiha.miniShop.ui.adapters.GroceryItemAdapter
import org.thiha.miniShop.utils.Utils

class ShowItemsByCategoryActivity : AppCompatActivity() {
    private var txtName: TextView? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: GroceryItemAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_items_by_category)
        initViews()
        adapter = GroceryItemAdapter(this)
        recyclerView!!.adapter = adapter
        recyclerView!!.layoutManager = GridLayoutManager(this, 2)
        try {
            val intent = intent
            val category = intent.getStringExtra("category")
            val utils = Utils(this)
            val items = utils.getItemsByCategory(category)
            adapter!!.setItems(items)
            txtName!!.text = category
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    private fun initViews() {
        Log.d(TAG, "initViews: started")
        txtName = findViewById(R.id.txtCategory)
        recyclerView = findViewById(R.id.recyclerView)
    }

    companion object {
        private const val TAG = "ShowItemsByCategoryActivity"
    }
}