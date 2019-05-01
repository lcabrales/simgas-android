package com.lcabrales.simgas.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lcabrales.simgas.BaseActivity
import com.lcabrales.simgas.MainActivity
import com.lcabrales.simgas.R
import com.lcabrales.simgas.databinding.ActivityLoginBinding
import com.lcabrales.simgas.model.session.User

class LoginActivity : BaseActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        setOnClickListeners()
        setImeOptions()
        subscribe()
    }

    private fun subscribe() {
        viewModel.showLoadingLiveData.observe(this, Observer(this::showLoading))
        viewModel.showToastLiveData.observe(this, Observer(this::showToast))
        viewModel.loginCompletedLiveData.observe(this, Observer(this::completeLogin))
        viewModel.enableLoginButtonLiveData.observe(this, Observer(this::enableLoginButton))
        viewModel.clearUserFieldLiveData.observe(this, Observer(this::clearUserField))
        viewModel.clearPasswordFieldLiveData.observe(this, Observer(this::clearPasswordField))
    }

    private fun setOnClickListeners() {
        binding.btnLogin.setOnClickListener { performLogin() }
    }

    private fun setImeOptions() {
        binding.etPassword.setOnEditorActionListener { _, action, _ ->
            var handled = false
            if (action == EditorInfo.IME_ACTION_SEND) {
                performLogin()
                handled = true
            }
            handled
        }
    }

    private fun performLogin() {
        hideKeyboard()

        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()

        viewModel.sendLoginRequest(username, password)
    }

    @UiThread
    private fun showLoading(show: Boolean) {
        binding.pbLoading.visibility = if (show) View.VISIBLE else View.GONE
    }

    @UiThread
    private fun showToast(@StringRes stringRes: Int) {
        Toast.makeText(this, getString(stringRes), LENGTH_SHORT).show()
    }

    @UiThread
    private fun enableLoginButton(enable: Boolean) {
        binding.btnLogin.isEnabled = enable
    }

    @UiThread
    private fun clearUserField(clear: Boolean) {
        if (!clear) return

        binding.etUsername.text = null
    }

    @UiThread
    private fun clearPasswordField(clear: Boolean) {
        if (!clear) return

        binding.etPassword.text = null
    }

    @UiThread
    private fun completeLogin(user: User) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
