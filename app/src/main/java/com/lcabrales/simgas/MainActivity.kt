package com.lcabrales.simgas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lcabrales.simgas.ui.sensors.SensorsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SensorsFragment.newInstance())
                    .commitNow()
        }
    }

}
