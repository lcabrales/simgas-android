package com.lcabrales.simgas.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.annotation.UiThread
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lcabrales.simgas.BaseBackArrowActivity
import com.lcabrales.simgas.R
import com.lcabrales.simgas.databinding.ActivityRegisterBinding
import com.lcabrales.simgas.di.ViewModelFactory
import com.lcabrales.simgas.model.session.RegisterRequest
import com.lcabrales.simgas.model.session.Role
import com.lcabrales.simgas.ui.main.MainActivity

class RegisterActivity : BaseBackArrowActivity() {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this))
            .get(RegisterViewModel::class.java)

        setupToolbar(binding.includeAppBar.toolbar)
        setOnClickListeners()
        setImeOptions()
        subscribe()
    }

    private fun subscribe() {
        viewModel.showLoadingLiveData.observe(this, Observer(this::showLoading))
        viewModel.showToastLiveData.observe(this, Observer(this::showToast))
        viewModel.showAlertLiveData.observe(this, Observer(this::showAlertDialog))
        viewModel.registerCompletedLiveData.observe(this, Observer { completeRegister() })
        viewModel.enableRegisterButtonLiveData.observe(this, Observer(this::enableRegisterButton))
    }

    private fun setOnClickListeners() {
        binding.btnRegister.setOnClickListener { performRegister() }
    }

    private fun setImeOptions() {
        binding.etConfirmPassword.setOnEditorActionListener { _, action, _ ->
            var handled = false
            if (action == EditorInfo.IME_ACTION_SEND) {
                performRegister()
                handled = true
            }
            handled
        }
    }

    private fun performRegister() {
        hideKeyboard()

        val request = RegisterRequest(
            Role.REGULAR.id,
            binding.etUsername.text.toString(),
            binding.etFirstName.text.toString(),
            binding.etLastName.text.toString(),
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString()
        )
        viewModel.sendRegisterRequest(request, binding.etConfirmPassword.text.toString())
    }

    @UiThread
    private fun showLoading(show: Boolean) {
        binding.pbLoading.visibility = if (show) View.VISIBLE else View.GONE
    }

    @UiThread
    private fun enableRegisterButton(enable: Boolean) {
        binding.btnRegister.isEnabled = enable
    }

    @UiThread
    private fun completeRegister() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
