package com.lcabrales.simgas

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder

abstract class BaseActivity : AppCompatActivity() {

    fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    open fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
    }

    @UiThread
    fun showToast(@StringRes stringRes: Int) {
        Toast.makeText(this, getString(stringRes), Toast.LENGTH_SHORT).show()
    }

    @UiThread
    fun showAlertDialog(string: String) {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle(R.string.alert_dialog_title)
        builder.setMessage(string)
        builder.setPositiveButton(R.string.alert_dialog_positive, null)
        builder.show()
    }
}