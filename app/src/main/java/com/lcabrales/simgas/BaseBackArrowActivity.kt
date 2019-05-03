package com.lcabrales.simgas

import android.view.MenuItem
import androidx.appcompat.widget.Toolbar

abstract class BaseBackArrowActivity : BaseActivity() {

    override fun setupToolbar(toolbar: Toolbar) {
        super.setupToolbar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}