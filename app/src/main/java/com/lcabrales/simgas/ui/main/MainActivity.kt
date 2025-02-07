package com.lcabrales.simgas.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
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
import com.lcabrales.simgas.ui.aboutus.AboutUsFragment
import com.lcabrales.simgas.ui.login.LoginActivity
import com.lcabrales.simgas.ui.profile.ProfileFragment
import com.lcabrales.simgas.ui.readinglevels.ReadingLevelsFragment
import com.lcabrales.simgas.ui.sensors.SensorsFragment

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var currentFragment: Fragment? = null
    private var menuRef: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this))
            .get(MainViewModel::class.java)

        setupToolbar(binding.includeAppBar.toolbar)
        setupNavigationDrawer()
        subscribe()

        viewModel.fetchUser()
    }

    private fun subscribe() {
        viewModel.headerLiveData.observe(this, Observer(this::populateNavigationHeader))
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
                    showFragment(SensorsFragment.newInstance(), menuItem.title)
                }
                R.id.nav_profile -> {
                    showFragment(ProfileFragment.newInstance(), menuItem.title)
                }
                R.id.nav_workshops -> {
                }
                R.id.nav_levels -> {
                    showFragment(ReadingLevelsFragment.newInstance(), menuItem.title)
                }
                R.id.nav_about_us -> {
                    showFragment(AboutUsFragment.newInstance(), menuItem.title)
                }
                R.id.nav_logout -> {
                    showLogoutDialog()
                }
            }

            true
        }

        val sensorsItem = binding.navView.menu.findItem(R.id.nav_sensors)
        binding.navView.setCheckedItem(sensorsItem)
        showFragment(SensorsFragment.newInstance(), sensorsItem.title)
    }

    private fun showFragment(fragment: Fragment, title: CharSequence) {
        supportActionBar?.title = title

        currentFragment = fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_container, fragment)
            .commitNow()

        setRefreshMenuVisibility()
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
    private fun populateNavigationHeader(fullName: String) {
        val headerView = binding.navView.getHeaderView(0)
        val tvTitle = headerView.findViewById<TextView>(R.id.tv_title)
        tvTitle.text = fullName
    }

    @UiThread
    private fun completeLogout() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sensors_menu, menu)
        menuRef = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_refresh -> {
                if (currentFragment is SensorsFragment) {
                    (currentFragment as SensorsFragment).refreshSensors()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setRefreshMenuVisibility() {
        val refreshMenu = menuRef?.findItem(R.id.menu_refresh)
        refreshMenu?.isVisible = currentFragment is SensorsFragment
    }
}
