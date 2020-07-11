package org.thiha.miniShop.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import org.thiha.miniShop.R
import org.thiha.miniShop.ui.dialogs.LicensesDialog
import org.thiha.miniShop.ui.dialogs.ShowAllCategoriesDialog
import org.thiha.miniShop.ui.fragments.MainFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var drawer: DrawerLayout? = null
    private var navigationView: NavigationView? = null
    private var toolbar: Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.drawer_open, R.string.drawer_closed)
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()
        navigationView!!.setNavigationItemSelectedListener(this)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, MainFragment())
        transaction.commit()
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.cart -> {
                val intent = Intent(this@MainActivity, CartActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            R.id.categories -> {
                val showAllCategoriesDialog = ShowAllCategoriesDialog()
                showAllCategoriesDialog.show(supportFragmentManager, "all categories")
            }
            R.id.about -> {
                AlertDialog.Builder(this@MainActivity)
                        .setTitle("About")
                        .setMessage("Instructed by Meisam and developed by Thiha Eung")
                        .setPositiveButton("OK", null).show()
            }
            R.id.terms -> {
                val termsBuilder = AlertDialog.Builder(this@MainActivity)
                        .setTitle("Terms of use")
                        .setMessage("No extra terms\n enjoy :)")
                        .setPositiveButton("OK") { _, _ -> }
                termsBuilder.create().show()
            }
            R.id.licenses -> {
                val licensesDialog = LicensesDialog()
                licensesDialog.show(supportFragmentManager, "licenses dialog")
            }
            else -> {
            }
        }
        return false
    }

    private fun initViews() {
        Log.d(TAG, "initViews: started")
        drawer = findViewById(R.id.drawer)
        navigationView = findViewById(R.id.navigationDrawer)
        toolbar = findViewById(R.id.toolbar)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}