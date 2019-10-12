package com.lcabrales.simgas

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment

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
    fun showToast(string: String) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }
}