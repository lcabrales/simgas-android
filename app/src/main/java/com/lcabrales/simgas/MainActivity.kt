package com.lcabrales.simgas

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.lcabrales.simgas.databinding.ActivityMainBinding
import com.lcabrales.simgas.ui.sensors.SensorsFragment

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupToolbar(binding.includeAppBar.toolbar)
        setupNavigationDrawer()
    }

    private fun setupNavigationDrawer() {
        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout,
            binding.includeAppBar.toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            binding.drawerLayout.closeDrawers()

            // Handle navigation view item clicks here.
            when (menuItem.itemId) {

                R.id.nav_sensors -> {
                    showFragment(SensorsFragment.newInstance())
                }
                R.id.nav_profile -> {
                }
                R.id.nav_workshops -> {
                }
                R.id.nav_levels -> {
                }
                R.id.nav_about_us -> {
                }
                R.id.nav_logout -> {
                    logout()
                }
            }

            true
        }

        populateNavigationHeader()
        binding.navView.setCheckedItem(R.id.nav_sensors)
        showFragment(SensorsFragment.newInstance())
    }

    private fun populateNavigationHeader() {
        //todo implement
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_container, fragment)
            .commitNow()
    }

    private fun logout() {
        //todo logout
    }
}
