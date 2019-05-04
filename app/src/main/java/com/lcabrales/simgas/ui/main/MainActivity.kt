package com.lcabrales.simgas.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.annotation.UiThread
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lcabrales.simgas.BaseActivity
import com.lcabrales.simgas.R
import com.lcabrales.simgas.databinding.ActivityMainBinding
import com.lcabrales.simgas.di.ViewModelFactory
import com.lcabrales.simgas.ui.login.LoginActivity
import com.lcabrales.simgas.ui.sensors.SensorsFragment

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this))
            .get(MainViewModel::class.java)

        setupToolbar(binding.includeAppBar.toolbar)
        setupNavigationDrawer()
        subscribe()
    }

    private fun subscribe() {
        viewModel.logoutCompletedLiveData.observe(this, Observer { completeLogout() })
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
                    showLogoutDialog()
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

    private fun showLogoutDialog() {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle(R.string.main_activity_logout_confirmation_title)
        builder.setMessage(R.string.main_activity_logout_confirmation_message)
        builder.setPositiveButton(
            R.string.main_activity_logout_confirmation_positive) { _, _ -> logout() }
        builder.setNegativeButton(R.string.main_activity_logout_confirmation_negative, null)
        builder.show()
    }

    private fun logout() {
        viewModel.performUserLogout()
    }

    @UiThread
    private fun completeLogout() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
