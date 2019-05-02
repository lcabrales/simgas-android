package com.lcabrales.simgas

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.lcabrales.simgas.databinding.ActivityMainBinding
import com.lcabrales.simgas.ui.sensors.SensorsFragment

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, SensorsFragment.newInstance())
                .commitNow()
        }

        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.includeAppBar.toolbar)
        supportActionBar?.title = title
    }

}
