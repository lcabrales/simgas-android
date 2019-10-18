package com.lcabrales.simgas

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

abstract class BaseFragment : Fragment() {

    fun hideKeyboard() {
        val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.rootView?.windowToken, 0)
    }

    @UiThread
    fun showToast(@StringRes stringRes: Int) {
        Toast.makeText(context, getString(stringRes), Toast.LENGTH_SHORT).show()
    }

    @UiThread
    fun showAlertDialog(string: String) {
        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle(R.string.alert_dialog_title)
        builder.setMessage(string)
        builder.setPositiveButton(R.string.alert_dialog_positive, null)
        builder.show()
    }
}